### postProcessorNames具体是哪些？

```java
		String[] postProcessorNames =
				beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);

	@Override
	public String[] getBeanNamesForType(@Nullable Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
        //allowEagerInit  = false
		if (!isConfigurationFrozen() || type == null || !allowEagerInit) {
            //所以会走这个分支
			return doGetBeanNamesForType(ResolvableType.forRawClass(type), includeNonSingletons, allowEagerInit);
		}
        //省略不相关代码
		return null;
	}
```
```java
	private String[] doGetBeanNamesForType(ResolvableType type, boolean includeNonSingletons, boolean allowEagerInit) {
        //type  =  BeanDefinitionRegistryPostProcessor  includeNonSingletons = true  allowEagerInit = false
		List<String> result = new ArrayList<>();

		// Check all bean definitions.
        //这里我们先关注一下 this.beanDefinitionNames
        //通过DEBUG 发现。总共有7个元素 见图1
        // 当然我们知道最后的返回值只有一个，也就是第一个元素 。
        // 那我们首先要想一下。第一个元素是从被加载上来的？
        // 如果正向跟代码查找比较费时 -> 所以我们逆向查找一下。 既然this.beanDefinitionNames是一个list元素。那么list的元素要加入到list容器中
        // 无非是两种情况。 add() addAll()
        // 我们将this.beanDefinitionNames 所对应的方法都加上断点。方便跟进
        // 随后我们在这个地方发现了添加了第一个元素（见图2） ，恰巧第一个元素就是这个方法的最终返回值
        //继续看图2 的左下角栈部分。找一下是从哪个地方调的。
        //最后发现是AnnotationConfigApplicationContext ，原来是这玩意弄进去的。
        //其实在 初探SpringBoot源码,springboot启动获取 上下文(3).md 中，带了一笔。当时没发现有偷偷干了这么一件事情。   最终把这个干活的是 org.springframework.context.annotation.AnnotationConfigUtils#registerAnnotationConfigProcessors(org.springframework.beans.factory.support.BeanDefinitionRegistry, java.lang.Object)
        
		for (String beanName : this.beanDefinitionNames) {
            //没有别名
			if (!isAlias(beanName)) {
				try {
					RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
					//一些判断。 肯定不是抽象的。 后续的条件只要有一个满足就会进if。 
                    //这里会进if分支
					if (!mbd.isAbstract() && (allowEagerInit ||
							(mbd.hasBeanClass() || !mbd.isLazyInit() || isAllowEagerClassLoading()) &&
									!requiresEagerInitForType(mbd.getFactoryBeanName()))) {
						boolean isFactoryBean = isFactoryBean(beanName, mbd);
						BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
						boolean matchFound = false;
						boolean allowFactoryBeanInit = (allowEagerInit || containsSingleton(beanName));
						boolean isNonLazyDecorated = (dbd != null && !mbd.isLazyInit());
						if (!isFactoryBean) {
							if (includeNonSingletons || isSingleton(beanName, mbd, dbd)) {
                                   //会进这个分支
                                   //这块与主线关联性不足（最主要的是里面又是一大坨代码），根据这个名称我们知道这个是按照类型匹配的。
                                   //结果肯定也是true的
								matchFound = isTypeMatch(beanName, type, allowFactoryBeanInit);
							}
						}
						if (matchFound) {
							result.add(beanName);
						}
					}
				}
			}
		}
		return StringUtils.toStringArray(result);
	}
```

![图1](F:\liming\work_space\my_work_space\java-study\img\spring\annex-5-2\1640846504.jpg)

图1



![image-20211230145423639](C:\Users\wangguochu\AppData\Roaming\Typora\typora-user-images\image-20211230145423639.png)

图2