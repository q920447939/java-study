**文章内容如有错误,请提出**

在上一章中，我们了解到springboot创建了context。以及为context组合了许多组件。比如scan,read 等等

那么在这一章中，我们将从spring boot 跳到spring

```java
//spring boot 最终会调用  org.springframework.context.support.AbstractApplicationContext#refresh
//细心的人会注意到，此时refresh方法所在的包已经不在spring boot中了。而是在spring 中
//当然如果你去找一找AbstractApplicationContext的类注释，会发现作者有很多人，而且这个类已经在2001年就产生了  但是经过查询spring官方 发现spring1.0版本既然是2004年(https://spring.io/blog/2004/03/24/spring-framework-1-0-final-released)... 有兴趣的同学可以看看
//其次 ，在类注释中 作者说的很清楚AbstractApplicationContext的作用。简而言之 这个类是一个接口是抽象实现，采用了一些模板方法设计模式，具体的一些方法可以让子类去实现。
//同时也举例了 与普通的BeanFactory 相比，ApplicationContext 应该检测在其内部 bean 工厂中定义的特殊 bean：因此，此类自动注册在上下文中定义为 bean 的 BeanFactoryPostProcessors、BeanPostProcessors 和 ApplicationListeners。 我理解是BeanFactory如果检测到这些bean，会对应的做一些事情

public void refresh() throws BeansException, IllegalStateException {
			// 准备刷新,主要是设置了 propertyResolver(属性解析器) 。通过ConfigurablePropertyResolver了解到 ，propertyResolver 拥有将一种属性值转换到另外的一种属性值
			prepareRefresh();

			//设置bean工厂id ,返回了一个有配置能力的bean工厂 ,其实返回的是DefaultListableBeanFactory
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

			// Prepare the bean factory for use in this context.
			prepareBeanFactory(beanFactory);
}			

	protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		// 设置类加载器
		beanFactory.setBeanClassLoader(getClassLoader());
        // spel检查 ,从配置文件(传入到main的运行参数)读取spring.spel.ignore 。没有读到的话就会创建一个bean的表达式解析器
        // 1. 类似于在xml中 。通过 sp el表达式 #{xxx} 。
        // 2. 在xml中。可能还拥有  A bean 引用B bean等操作的转换
		if (!shouldIgnoreSpel) {
            // StandardBeanExpressionResolver 类中。还拥有 这两个属性 ,这更加确定了1 的猜想
            /** Default expression prefix: "#{". */
            // public static final String DEFAULT_EXPRESSION_PREFIX = "#{";
            /** Default expression suffix: "}". */
            //public static final String DEFAULT_EXPRESSION_SUFFIX = "}";
			beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
		}
        //bean工厂增加一个属性编辑器  上面的是表达式解析器,这里是编辑器
		beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));

		//配置后置处理器。  ApplicationContextAwareProcessor  Aware=意识到,感知  
		beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
		beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
		beanFactory.ignoreDependencyInterface(EmbeddedValueResolverAware.class);
		beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
		beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
		beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
		beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);
		beanFactory.ignoreDependencyInterface(ApplicationStartupAware.class);

		// BeanFactory interface not registered as resolvable type in a plain factory.
		// MessageSource registered (and found for autowiring) as a bean.
        // 大概猜想是注册这些bean的时候,需要依赖某些东西
		beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
		beanFactory.registerResolvableDependency(ResourceLoader.class, this);
		beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
		beanFactory.registerResolvableDependency(ApplicationContext.class, this);

		// Register early post-processor for detecting inner beans as ApplicationListeners.
        //又注册了一个后置处理器
		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));

		// Detect a LoadTimeWeaver and prepare for weaving, if found.
        //这块不知道具体含义是什么.但是根据DEBUG信息 没有来到这块 暂且忽略
		if (!NativeDetector.inNativeImage() && beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
			beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
			// Set a temporary ClassLoader for type matching.
			beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
		}

		// Register default environment beans.
        //注册一些环境bean
		if (!beanFactory.containsLocalBean(ENVIRONMENT_BEAN_NAME)) { //ENVIRONMENT_BEAN_NAME = environment
			beanFactory.registerSingleton(ENVIRONMENT_BEAN_NAME, getEnvironment());
		}
		if (!beanFactory.containsLocalBean(SYSTEM_PROPERTIES_BEAN_NAME)) {//SYSTEM_PROPERTIES_BEAN_NAME =  systemProperties
			beanFactory.registerSingleton(SYSTEM_PROPERTIES_BEAN_NAME, getEnvironment().getSystemProperties());
		}
		if (!beanFactory.containsLocalBean(SYSTEM_ENVIRONMENT_BEAN_NAME)) {//SYSTEM_ENVIRONMENT_BEAN_NAME = systemEnvironment
			beanFactory.registerSingleton(SYSTEM_ENVIRONMENT_BEAN_NAME, getEnvironment().getSystemEnvironment());
		}
		if (!beanFactory.containsLocalBean(APPLICATION_STARTUP_BEAN_NAME)) {// APPLICATION_STARTUP_BEAN_NAME = applicationStartup
			beanFactory.registerSingleton(APPLICATION_STARTUP_BEAN_NAME, getApplicationStartup());
		}
	}

```

### 总结

1.在准备阶段,`spring`组合了表达式解析器，属性编辑器  。注册了两个BeanPostProcessor(`ApplicationContextAwareProcessor`,`ApplicationListenerDetector`)

同时注册了4个单例bean (`environment`,`systemProperties`,`systemEnvironment`,`applicationStartup`)。

