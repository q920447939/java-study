**文章内容如有错误,欢迎指出**

本章内容涉及的内容非常多.postProcess ,getBean

三级缓存等

入口是这3个函数

我们逐一分析

```java
				// Allows post-processing of the bean factory in context subclasses.
				//钩子函数,让子类去实现.这里没有实现逻辑
				postProcessBeanFactory(beanFactory);

				// Invoke factory processors registered as beans in the context.
				//调用工厂处理bean作为上下文				
				invokeBeanFactoryPostProcessors(beanFactory);

				// Register bean processors that intercept bean creation.
				registerBeanPostProcessors(beanFactory);

```





先分析`invokeBeanFactoryPostProcessors(beanFactory);`方法   

```java
protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
//这里使用了委派模式    
    //首先调用 getBeanFactoryPostProcessors() 获取了 beanFactoryPostProcessors
    PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());

   // Detect a LoadTimeWeaver and prepare for weaving, if found in the meantime
   // (e.g. through an @Bean method registered by ConfigurationClassPostProcessor)
   if (!NativeDetector.inNativeImage() && beanFactory.getTempClassLoader() == null && beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
      beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
      beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
   }
}

public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
    return this.beanFactoryPostProcessors;
}


//通过类名可知，这个方法主要是调用bean工厂后置处理器
public static void invokeBeanFactoryPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {

		// Invoke BeanDefinitionRegistryPostProcessors first, if any.
		Set<String> processedBeans = new HashSet<>();

    	//这里是DefaultListableBeanFactory 。 DefaultListableBeanFactory 也是实现了BeanDefinitionRegistry接口  会走if逻辑
		if (beanFactory instanceof BeanDefinitionRegistry) {
			BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
            //普通的postProcess集合
			List<BeanFactoryPostProcessor> regularPostProcessors = new ArrayList<>();
            //注册postProcess集合
			List<BeanDefinitionRegistryPostProcessor> registryProcessors = new ArrayList<>();
			// 这里  beanFactoryPostProcessors 循环 .调用 如果process 实现了 BeanDefinitionRegistryPostProcessor  ,那么就会调用postProcessBeanDefinitionRegistry方法 并且加入到 registryProcessors列表中 ,否则的话就会把他加到普通的postProcess集合中    
            //beanFactoryPostProcessors 通过DEBUG发现是有两个值的 
            //分别是  org.springframework.boot.autoconfigure.SharedMetadataReaderFactoryContextInitializer$CachingMetadataReaderFactoryPostProcessor
           	 //	org.springframework.boot.context.ConfigurationWarningsApplicationContextInitializer$ConfigurationWarningsPostProcessor
            //不过这里当时DEBUG的时的时候产生了一点疑惑. 是在什么地方把这两个对象放到beanFactoryPostProcessors
            //感兴趣可参考 初探Spring源码,后置处理阶段(5.附1).md
			for (BeanFactoryPostProcessor postProcessor  : beanFactoryPostProcessors) {
				if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
					BeanDefinitionRegistryPostProcessor registryProcessor =
							(BeanDefinitionRegistryPostProcessor) postProcessor;
                    //beanFactoryPostProcessors 共有两个 。
                    //CachingMetadataReaderFactoryPostProcessor 调用postProcessBeanDefinitionRegistry方法后 
                    	//注册了一个SharedMetadataReaderFactoryBean beandifinition  名字叫做 org.springframework.boot.autoconfigure.internalCachingMetadataReaderFactory  
                    	// 将definition 的属性 propertyValues 增加值 
                    	// propertyValues.add("metadataReaderFactory", new RuntimeBeanReference(BEAN_NAME));
                    //ConfigurationWarningsPostProcessor 主要是检查启动类上面是否有@ComponentScan注解。如果有的话 是否包含org 或者org.springframework 。有的话报错	测试看图1
					registryProcessor.postProcessBeanDefinitionRegistry(registry);
					registryProcessors.add(registryProcessor);
				}
				else {
                    //else 不会进来
					regularPostProcessors.add(postProcessor);
				}
			}

			// Do not initialize FactoryBeans here: We need to leave all regular beans
			// uninitialized to let the bean factory post-processors apply to them!
			// Separate between BeanDefinitionRegistryPostProcessors that implement
			// PriorityOrdered, Ordered, and the rest.
            // 不要在此处初始化 FactoryBeans：我们需要保留所有常规 bean
            // 未初始化以让 bean 工厂后处理器应用于它们！
            // 分离实现的 BeanDefinitionRegistryPostProcessors
            // PriorityOrdered、Ordered 和其他。
			List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();

			// First, invoke the BeanDefinitionRegistryPostProcessors that implement PriorityOrdered. 第一步，将后置处理器先进行优先级排序
            //beanFactory.getBeanNamesForType 这个方法暂时没有去研究，不过通过名字可知，应该是通过传入BeanDefinitionRegistryPostProcessor ，找到它的一些实现类。
            //有兴趣的同学可以看 初探Spring源码,后置处理阶段(5.附2).md
            //这里postProcessorNames只会有一个  org.springframework.context.annotation.ConfigurationClassPostProcessor
			String[] postProcessorNames =
					beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			for (String ppName : postProcessorNames) {
                // 这里既然没有else 那么证明只会取实现了PriorityOrdered接口的postProcess
				if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					processedBeans.add(ppName);
				}
			}
            //进行排序
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			registryProcessors.addAll(currentRegistryProcessors);
             //这部分篇幅较长。将移到 初探Spring源码,后置处理阶段(5.1).md
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry, beanFactory.getApplicationStartup());
            //将容器清理
			currentRegistryProcessors.clear();

			// Next, invoke the BeanDefinitionRegistryPostProcessors that implement Ordered.
            //这里再次调用getBeanNamesForType()方法。
			postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			for (String ppName : postProcessorNames) {
                //不同的点在于这个条件。processedBeans容器里面没有的 根据后面的代码推断。 因为如果没有这个判断的话。那么org.springframework.context.annotation.ConfigurationClassPostProcessor相同的代码又会执行一次
                //并且这个类需要实现Ordered接口。上面的是实现PriorityOrdered接口。 两个是不同的。
                //所以如果实现了PriorityOrdered会比实现Ordered的先调用
				if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					processedBeans.add(ppName);
				}
			}
            //排序
			sortPostProcessors(currentRegistryProcessors, beanFactory);
            //放到registryProcessors里面。 
			registryProcessors.addAll(currentRegistryProcessors);
            //还是调用之前的方法。只是 currentRegistryProcessors 里面的东西不同
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry, beanFactory.getApplicationStartup());
			currentRegistryProcessors.clear();

			// Finally, invoke all other BeanDefinitionRegistryPostProcessors until no further ones appear.
            // 最后，调用所有其他 BeanDefinitionRegistryPostProcessor 直到不再出现。
            //这里依旧还是调用beanFactory.getBeanNamesForType 然后再调用invokeBeanDefinitionRegistryPostProcessors
            // 思考 
            //为什么会三次（或者说多次，因为这里是while ）调用beanFactory.getBeanNamesForType呢？  
            //前面两次由于需要对实现了PriorityOrdered和Ordered的 BeanDefinitionRegistryPostProcessor做优先处理
            //这里使用while的原因是。如果我们自定义的类 。也同样实现了BeanDefinitionRegistryPostProcessor接口。但是没有实现PriorityOrdered和Ordered接口。那么就只能放到这里进行最后的调用了。
            //但是可能在我们自己实现的BeanDefinitionRegistryPostProcessor子类中。我们手动又增加了新的放到bean工厂中。那么我们就需要再次调用beanFactory.getBeanNamesForType。防止漏掉手动新增的BeanDefinitionRegistryPostProcessor子类
			boolean reiterate = true;
			while (reiterate) {
				reiterate = false;
				postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
				for (String ppName : postProcessorNames) {
					if (!processedBeans.contains(ppName)) {
						currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
						processedBeans.add(ppName);
						reiterate = true;
					}
				}
				sortPostProcessors(currentRegistryProcessors, beanFactory);
				registryProcessors.addAll(currentRegistryProcessors);
                 //会调用 BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry
				invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry, beanFactory.getApplicationStartup());
				currentRegistryProcessors.clear();
			}

			// Now, invoke the postProcessBeanFactory callback of all processors handled so far.
            //这里会回调所有实现了BeanFactoryPostProcessor.postProcessBeanDefinitionRegistry的方法。参考com.example.demo.MyBeanDefinitionRegistryPostProcessor。 
            //1.registryProcessors 也是前面每次把currentRegistryProcessors里面的数据放到容器里面了。而currentRegistryProcessors又是从bean工厂里面拿出来的。所以如果想要调用postProcessBeanDefinitionRegistry成功。那么需要让bean注册到bean工厂里面去
            //2.虽然MyBeanDefinitionRegistryPostProcessor没有单独实现BeanFactoryPostProcessor。但是因为 MyBeanDefinitionRegistryPostProcessor 实现了BeanDefinitionRegistryPostProcessor  。而BeanDefinitionRegistryPostProcessor接口又继承了 BeanFactoryPostProcessor  所以  MyBeanDefinitionRegistryPostProcessor instanceof BeanFactoryPostProcessor 肯定是true
			invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);
			invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
		}

		else {
			// Invoke factory processors registered with the context instance.
			invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
		}

		// Do not initialize FactoryBeans here: We need to leave all regular beans
		// uninitialized to let the bean factory post-processors apply to them!
    	// 不要在此处初始化 FactoryBeans：我们需要保留所有常规 bean
    	//未初始化让bean factory后处理器应用到他们身上！
        // 这里再次根据类型去获取postProcessor
		String[] postProcessorNames =
				beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);

		// Separate between BeanFactoryPostProcessors that implement PriorityOrdered,
		// Ordered, and the rest.
		List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
		List<String> orderedPostProcessorNames = new ArrayList<>();
		List<String> nonOrderedPostProcessorNames = new ArrayList<>();
		for (String ppName : postProcessorNames) {
			if (processedBeans.contains(ppName)) {
				// skip - already processed in first phase above
                //如果process已经处理过了。那么跳过处理
			}
			else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
                //优先级容器
				priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
			}
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				orderedPostProcessorNames.add(ppName);
			}
			else {
                //普通容器
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// First, invoke the BeanFactoryPostProcessors that implement PriorityOrdered.
        //优先级process排序
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
         //调用PostProcessors
		invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);

		// Next, invoke the BeanFactoryPostProcessors that implement Ordered.
		List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<>(orderedPostProcessorNames.size());
		for (String postProcessorName : orderedPostProcessorNames) {
			orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		sortPostProcessors(orderedPostProcessors, beanFactory);
		invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);

		// Finally, invoke all other BeanFactoryPostProcessors.
		List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<>(nonOrderedPostProcessorNames.size());
		for (String postProcessorName : nonOrderedPostProcessorNames) {
			nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);
    	//考虑到流程与上面的流程雷同。
        //只能猜测到。后面的这块重新获取beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false); 可能是因为走了else流程。else流程里面没有进行while循环调用。所以可能又会产生新的postprocess 

		// Clear cached merged bean definitions since the post-processors might have
		// modified the original metadata, e.g. replacing placeholders in values...
         //清除缓存的合并 bean 定义，因为后处理器可能有 修改了原始元数据，例如 替换值中的占位符...
         //也就是说 原来保存的definition 。可能由于后置处理器修改了对象的属性。 所以需要清空definition
		beanFactory.clearMetadataCache();
	}
```

![image-20211110172953755](../img/spring/image-20211110172953755.png)

图1



<iframe id="embed_dom" name="embed_dom" frameborder="0" style="display:block;width:1200px; height:800px;" src="https://www.processon.com/embed/61ce663a0e3e7441570a1d68"></iframe>

### 总结：

在`org.springframework.context.support.PostProcessorRegistrationDelegate#invokeBeanFactoryPostProcessors(org.springframework.beans.factory.config.ConfigurableListableBeanFactory, java.util.List<org.springframework.beans.factory.config.BeanFactoryPostProcessor>)`方法中。

1.首先通过入参传过来的 ` java.util.List<org.springframework.beans.factory.config.BeanFactoryPostProcessor>` 。循环调用`org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry`  这块可参考附1.md

2.通过





### 彩蛋：

最上方的`invokeBeanFactoryPostProcessors`方法中。还有一段如下的代码：

```java
		// Detect a LoadTimeWeaver and prepare for weaving, if found in the meantime
		// (e.g. through an @Bean method registered by ConfigurationClassPostProcessor)
		if (!NativeDetector.inNativeImage() && beanFactory.getTempClassLoader() == null && beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
			beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
			beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
		}
```

当时看到`NativeDetector.inNativeImage()`也是一脸懵逼不知道是个什么情况。但是点开一看`NativeDetector`类一看就猜出来一个大概：

```java

/**
 * A common delegate for detecting a GraalVM native image environment.
 *
 * <p>Requires using the {@code -H:+InlineBeforeAnalysis} native image compiler flag in order to allow code removal at
 * build time.
 *
 * @author Sebastien Deleuze
 * @since 5.3.4
 */
public abstract class NativeDetector {

	// See https://github.com/oracle/graal/blob/master/sdk/src/org.graalvm.nativeimage/src/org/graalvm/nativeimage/ImageInfo.java
	private static final boolean imageCode = (System.getProperty("org.graalvm.nativeimage.imagecode") != null);

	/**
	 * Returns {@code true} if invoked in the context of image building or during image runtime, else {@code false}.
	 */
	public static boolean inNativeImage() {
		return imageCode;
	}
}
```

瞧。。这不就是大名鼎鼎的[graalvm](https://github.com/oracle/graal)吗。

原来`spring`团队这么早就布局了`GraalVM`

同时大家也可以去了解一下为什么需要`GraalVM`以及`云原生`

关于`spring native`支持可以查看[github](https://github.com/spring-projects-experimental/spring-native) 

