## spring

1,bean的生命周期



**<a id="t1"> 通过BeanFactoryPostProcessor动态注入</a>**

```java
  DefaultListableBeanFactory df = (DefaultListableBeanFactory)configurableListableBeanFactory;
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Teacher.class);
        builder.addPropertyValue("name","王老师");
        String beanName = "teacher";
		//方法1  生成beandefinition 。然后注册bean
        df.registerBeanDefinition(beanName, builder.getRawBeanDefinition());
		//方法2 调用registerSingleton，注册单例bean
        //df.registerSingleton(beanName,new Teacher());
```

**<a id="t2"> 使用加密的属性文件</a>**

当读取spring bean属性解析的时候，比如bean 有一个属性配置的是${passsword} ，那么正常的情况下是去读取properties的明文信息，这样的话不太友好。

可以使用加密的方式，对properties的信息进行加密处理，然后通过PropertyPlaceholderConfigurer进行加密数据解密

example

```java
1.首先创建加解密工具类
    public class DESUtils {
    private static Key key;
    private static String KEY_STR = "myKey"; //秘钥

    static {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("DES");
            generator.init(new SecureRandom(KEY_STR.getBytes()));
            key = generator.generateKey();
            generator = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对str进行DES加密
     *
     * @param str
     * @return
     */
    public static String getEncryptString(String str) {
        BASE64Encoder base64en = new BASE64Encoder();
        try {
            byte[] strBytes = str.getBytes("UTF8");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptStrBytes = cipher.doFinal(strBytes);
            return base64en.encode(encryptStrBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对str进行DES解密
     *
     * @param str
     * @return
     */
    public static String getDecryptString(String str) {
        BASE64Decoder base64De = new BASE64Decoder();
        try {
            byte[] strBytes = base64De.decodeBuffer(str);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptStrBytes = cipher.doFinal(strBytes);
            return new String(decryptStrBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 1) {
            System.out.println("请输入要加密的字符，用空格分隔.");
        } else {
            for (String arg : args) {
                System.out.println(arg + ":" + getEncryptString(arg));
            }
        }
      System.out.println(getDecryptString("jt2eWexgLG41="));      //System.out.println(getDecryptString("gJQ9O+q34qk="));
    }
```

2.对占位符需要解析的类做处理

```java
@Component
public class EncryptPropertiesPlaceholderConfig extends PropertyPlaceholderConfigurer {
    private String [] encryptProNames = new String[]{"name"};

    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        //当属性值为name时，那么使用des进行解密
        if (Arrays.asList(encryptProNames).contains(propertyName)) {
            return DESUtils.getDecryptString(propertyValue);
        }
        return super.convertProperty(propertyName, propertyValue);
    }
}

```

3.xml配置占位符类信息，包括需要扫描的路径

```xml
    <!--3.使用加密版的属性文件  -->
    <bean class="demo.des.EncryptPropertiesPlaceholderConfig">
        <property name="location" value="/application.propertites"/>
    </bean>
```

4.application.propertites配置

```properties
name=e1MY0AEk4oiIavT6a8A13A==
```



5.创建一个bean， 获取name

```java
//bean 信息如下
@Component(value = "teacher")
@Data
public class Teacher {
    @Value("${name}")
    String name;
}

```



6.启动spring ，测试 输出结果

```java
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        Teacher teacher = context.getBean("teacher",Teacher.class);
        System.err.println(teacher.getName()); //输出结果为ab娃哈哈
    }
```



7.原理解析

当调用invokeBeanFactoryPostProcessors(beanFactory); 时， 会把实现了BeanFactoryPostProcessors的类全部拿出来做加工，可以理解为bean最开始被放到beandefinitionRegister ，那个时候的beandefinition是一个半成品，还需要进行加工才能是一个成品beandefinition（也就是处理postProcessor方法）

 [调用链图](img/placeholderConfig.jpg)

7.1翻到代码 （版本spring-context-4.3.22.RELEASE.jar）

```java
org.springframework.context.support.PostProcessorRegistrationDelegate#invokeBeanFactoryPostProcessors(org.springframework.beans.factory.config.ConfigurableListableBeanFactory, java.util.List<org.springframework.beans.factory.config.BeanFactoryPostProcessor>)

    1.按照类型获取到当前bean工厂里面实现了BeanFactoryPostProcessor.class的子类
String[] postProcessorNames =
				beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);
    
	2.随后调用 org.springframework.context.support.PostProcessorRegistrationDelegate#invokeBeanFactoryPostProcessors(java.util.Collection<? extends org.springframework.beans.factory.config.BeanFactoryPostProcessor>, org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
        
    3.因为EncryptPropertiesPlaceholderConfig没有直接实现BeanFactoryPostProcessors，所以会调用父类PropertyResourceConfigurer#postProcessBeanFactory
     4.在postProcessBeanFactory方法中，会调用convertProperties(mergedProps); 

5.convertProperties代码如下	
	protected void convertProperties(Properties props) {
		Enumeration<?> propertyNames = props.propertyNames();
		while (propertyNames.hasMoreElements()) {
			String propertyName = (String) propertyNames.nextElement();
			String propertyValue = props.getProperty(propertyName);
			String convertedValue = convertProperty(propertyName, propertyValue); // 这里是因为子类（EncryptPropertiesPlaceholderConfig）重写了convertProperty方法，所以会调用EncryptPropertiesPlaceholderConfig类中的方法（也就是我们自定义的方法）
			if (!ObjectUtils.nullSafeEquals(propertyValue, convertedValue)) {
				props.setProperty(propertyName, convertedValue); //转换完值后，再设置对应的属性
			}
		}
	}
```







