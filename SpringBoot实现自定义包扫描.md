## SpringBoot实现自定义包扫描

最近很好奇在SpringBoot项目上加`@MapperScan(basePackages = "xxx")` 注解就能扫描到执行的包下面的东西。于是研究了一下Mybatis怎么实现的。大致是根据Mybatis依葫芦画瓢

于是点开`@MapperScan`类,看到类结构如下:

```java

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({MapperScannerRegistrar.class})  //MapperScannerRegistrar这个类才是真正的注册逻辑
@Repeatable(MapperScans.class)
public @interface MapperScan {
    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};
	
    ...
}


```



然后点开`MapperScannerRegistrar`,看一下类结构

```java

//当使用@Import标签,实现ImportBeanDefinitionRegistrar 接口,那么可以自定义扩展beanDefinition
public class MapperScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    
    //资源加载器
    private ResourceLoader resourceLoader;


    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //从启动类上面获取MapperScan注解
        AnnotationAttributes mapperScanAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(MapperScan.class.getName()));
        if (mapperScanAttrs != null) {
            //存在的话那么注册自己的BeanDefinitions
            this.registerBeanDefinitions(mapperScanAttrs, registry);
        }

    }

    //已将不重要的内容删除
    void registerBeanDefinitions(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry) {
        //自定义注解扫描器
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);

        Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
        if (!BeanNameGenerator.class.equals(generatorClass)) {
           //这里是对应处理bean scanner.setBeanNameGenerator((BeanNameGenerator)BeanUtils.instantiateClass(generatorClass));
        }
       //获取注解basePackages value   basePackages.addAll((Collection)Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText).collect(Collectors.toList()));
        basePackages.addAll((Collection)Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName).collect(Collectors.toList()));
        scanner.registerFilters();
        //获取到包,开始进行扫描, 
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }
}

```



这里是doScan的逻辑,Spring的代码中,一般已do开头的都是具体做事情的,这里返回扫描包下面的的`beanDefinitions`集合,如果存在的话,那么会调用对应的`BeanNameGenerator` 方法,对应上面`scanner.setBeanNameGenerator`

```jav
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        return beanDefinitions;
    }
```





大概思路了解了,那我们也整一个

1. 定义启动类需要配置的扫描注解

   ```java
   
   import org.springframework.context.annotation.Import;
   
   import java.lang.annotation.Documented;
   import java.lang.annotation.ElementType;
   import java.lang.annotation.Retention;
   import java.lang.annotation.RetentionPolicy;
   import java.lang.annotation.Target;
   
   /**
    * ClassName: MyAOP
    * @Description:
    * @author leegoo
    * @date 2020年03月14日
    */
   @Retention(RetentionPolicy.RUNTIME)//注意用这个注解才能在运行时使用反射
   @Target({ElementType.TYPE})
   @Documented
   @Import({CustomerScanRegister.class})
   public @interface CustomerScan {
       //扫描包路径
       String[] basePackages() default {};
       //扫描类
       Class<?>[] basePackageClasses() default {};
   }
   
   ```

2. 定义`CustomerScanRegister`以及实现

   ```java
   
   import org.springframework.beans.BeansException;
   import org.springframework.beans.factory.BeanFactory;
   import org.springframework.beans.factory.BeanFactoryAware;
   import org.springframework.beans.factory.config.BeanDefinitionHolder;
   import org.springframework.beans.factory.support.BeanDefinitionRegistry;
   import org.springframework.context.ResourceLoaderAware;
   import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
   import org.springframework.core.annotation.AnnotationAttributes;
   import org.springframework.core.io.ResourceLoader;
   import org.springframework.core.type.AnnotationMetadata;
   import org.springframework.lang.NonNull;
   import org.springframework.util.ClassUtils;
   import org.springframework.util.StringUtils;
   
   import java.util.ArrayList;
   import java.util.Arrays;
   import java.util.List;
   import java.util.Set;
   import java.util.stream.Collectors;
   
   
   public class CustomerScanRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanFactoryAware {
       private ResourceLoader resourceLoader;
   
       private BeanFactory beanFactory;
   
       @Override
       public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
           this.beanFactory = beanFactory;
       }
   
       @Override
       public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
           this.resourceLoader = resourceLoader;
       }
   
       @Override
       public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,@NonNull BeanDefinitionRegistry registry) {
           //这里是获取cn.withmes.springboot.my.aop.SpringBootMyAopApplication类上对应的注解
           //MergedAnnotations annotations = importingClassMetadata.getAnnotations();
           //这里判断是否存在MyAOP注解
           AnnotationAttributes mapperScanAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(CustomerScan.class.getName()));
           if (mapperScanAttrs == null)  return;
   
           this.registerBeanDefinitions(mapperScanAttrs, registry);
       }
   
   
       private  Set<BeanDefinitionHolder> registerBeanDefinitions(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry) {
           List<String> basePackages = new ArrayList<>();
           //取到所有属性的值
           basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText).collect(Collectors.toList()));
           basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName).collect(Collectors.toList()));
           CustomerScanner scanner = new CustomerScanner(registry);
           scanner.setBeanNameGenerator(( beanDefinition,beanDefinitionRegistry)->{
               String beanClassName = beanDefinition.getBeanClassName();
               try {
                   Class<?> clz = Class.forName(beanClassName);
                   MyService at = clz.getAnnotation(MyService.class);
                   if (null == at) return null;
                   //如果@MyService没有指定名字,那么默认首字母小写进行注册
                   if (at.name().equalsIgnoreCase("")  ) {
                       String clzSimpleName = clz.getSimpleName();
                       String first = String.valueOf(clzSimpleName.charAt(0));
                       return clzSimpleName.replaceFirst(first,first.toLowerCase());
                   }
                   return at.name();
               } catch (ClassNotFoundException e) {
                   e.printStackTrace();
                   return null;
               }
           });
           if(resourceLoader != null){
               scanner.setResourceLoader(resourceLoader);
           }
           
           return scanner.doScan(StringUtils.toStringArray(basePackages));
       }
   
   
   
   ```

3. 自定义扫描器

   ```java
   
   import org.springframework.beans.factory.config.BeanDefinitionHolder;
   import org.springframework.beans.factory.support.BeanDefinitionRegistry;
   import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
   import org.springframework.core.type.filter.AnnotationTypeFilter;
   
   import java.util.Set;
   
   
   public class CustomerScanner extends ClassPathBeanDefinitionScanner {
   
       public CustomerScanner(BeanDefinitionRegistry registry) {
           super(registry, false);
       }
   
       @Override
       public Set<BeanDefinitionHolder> doScan(String... basePackages) {
           //添加过滤条件，这里是只添加了@MyService的注解才会被扫描到
           addIncludeFilter(new AnnotationTypeFilter(MyService.class));
           return super.doScan(basePackages);
       }
   
   }
   
   ```

4. 写一个接口和两个实现类,试下不同的方式注入(这里把所有类的代码放在一起了)

   ```java
   
   public interface UserService {
       User findUser(Integer id) ;
   }
   
   
   @MyService // 使用自定义注解,注入spring容器
   public class UserServiceImpl implements UserService {
   
       @Resource
       private Data data;
   
       @Override
       public User findUser(Integer id)  {
           return data.users.get(id);
       }
   
   }
   
   @MyService(name = "lsUser") // 使用自定义注解,注入spring容器
   public class UserServiceImpl2 implements UserService {
   
       @Resource
       private Data data;
   
       @Override
       public User findUser(Integer id)  {
           return data.users.get(id);
       }
   
   }
   
   
   //实现CommandLineRunner,应用初始化后,去执行一段代码块逻辑,这段初始化代码在整个应用生命周期内只会执行一次
   @Service
   public class Data  implements CommandLineRunner {
       public Map<Integer, User> users = new HashMap<>(
   
       );
   
   
       @Override
       public void run(String... args) throws Exception {
           users.put(1, new User(1, "小红"));
           users.put(2, new User(2, "小明"));
           users.put(3, new User(3, "小三"));
           System.out.println("初始化数据:"+users);
       }
   
       public User getUsers(Integer id) {
           return users.get(id);
       }
   
   }
   
   
   ```

5. 开始测试

   ```java
   @SpringBootTest
   class SpringBootMyAopApplicationTests {
   
       //这里userServiceImpl 会报红,但是还是能够执行,因为我们没有使用spring注解进行Bean注入,所以会提示我们可能找不到bean
       @Resource(name = "userServiceImpl")
       private UserService  userService;
   
       @Resource(name = "lsUser")
       private UserService  lsUser;
   
       @Test
       void testCustomerAnnotation() {
           User user = userService.findUser(1);
           System.out.println("user:"+user); //user:User{id=1, name='小红'}
           User ls = lsUser.findUser(2);
           System.out.println("ls:"+ls);//ls:User{id=2, name='小明'}
       }
   
   }
   ```

   