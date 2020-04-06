## 基于Dubbo,本地Mock方案



### 起因

​	分布式系统中，由于系统之间相互依赖。导致开发人员对一个功能需要进行本地测试的时候，极有可能由于依赖的系统过多导致需要本地启动很多服务，这样严重影响我们的自测效率

​	虽然dubbo提供了mock,但是在spring环境下该mock是配置在xml里面的，有可能导致不小心提交了xml mock到生产环境，从而导致生产环境调用使用了mock数据，从而引发不可预知的问题

​	

## 实现原理

​	基于spring BeanPostProcessor + 动态代理



## 具体实现

1. 处理BeanPostProcessor,因为dubbo注册的bean之后肯定会经过BeanPostProcessor 

```java

@Component
public class HelloServiceInjectProcessor implements BeanPostProcessor, BeanFactoryAware {

    @Autowired
    private ApplicationContext applicationContext;

    private BeanFactory beanFactory;

    @Autowired
    private BeanDefinitionRegistryPostProcessor beanDefinitionRegistry;

    private  Class<?> targetCls ;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        this.targetCls =  bean.getClass();
        Class<?> targetCls = bean.getClass();
        Field[] targetFld = targetCls.getDeclaredFields();
        if (beanName.contains("messageImpl")){ //这里是demo,所以指定beanName = messageImpl 才能进来,自定义方案可以在这里控制
            for (Field field : targetFld) {
                //只对类中属性标记了Service,Resource注解的属性才进行处理
                if (field.isAnnotationPresent(Service.class)  || field.isAnnotationPresent(Resource.class)) {
                    if (!field.getType().isInterface()) { //JDK动态代理只支持接口
                        throw new BeanCreationException("RoutingInjected field must be declared as an interface:" + field.getName()
                                + " @Class " + targetCls.getName());
                    }
                    try {
                        this.handleRoutingInjected(field, bean, field.getType());
                    } catch (IllegalAccessException | IOException | NoSuchMethodException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return bean;
    }

    private void handleRoutingInjected(Field field, Object bean, Class<?> targetCls) throws IllegalAccessException, IOException, NoSuchMethodException, ClassNotFoundException {
        field.setAccessible(true);
        String injectVal = field.getName();
        User user = (User) Proxy.newProxyInstance(bean.getClass().getClassLoader(), new Class[]{targetCls}, new MyInvocationHandler(field));
        field.set(bean,user); //重置属性值,当调用该属性的方法时,其实会通过动态代理调用 MyInvocationHandler#invoke 方法
    }

}
```



2. 处理执行逻辑

```java

public class MyInvocationHandler implements InvocationHandler {

    private Object target; //被代理对象的引用作为一个成员变量保存下来了

    public MyInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equalsIgnoreCase("getName")){ //如果目标类调用 getName()方法,那么直接返回我们需要指定的数据 ,这里可以做成配置, 那么就不会变成死代码了
            return "你牛逼啥子?";
        }
        return method.invoke(this.target, args);
    }
}

```



当我们调用该方法时,原来可能会提示dubbo provider 没有注册,现在会返回`你牛逼啥子?`

```java
 ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        MessageImpl bean = context.getBean("messageImpl", MessageImpl.class);
        String name = bean.getName();//你牛逼啥子?
```







## 总结

1. 以上就能实现自定义mock,又不需要增加mock类,防止出现生产mock问题
2. 当然如果使用Mock框架也可以,不过test的时候需要硬编码,比较麻烦,这种方式可以通过配置,减少工作量