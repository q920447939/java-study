## invokeBeanDefinitionRegistryPostProcessors

翻译成中文大概是 调用bean定义注册后置处理器 。说起来怎么这么绕口呢。。

```java
	private static void invokeBeanDefinitionRegistryPostProcessors(
			Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry, ApplicationStartup applicationStartup) {
		//postProcessors   org.springframework.context.annotation.ConfigurationClassPostProcessor
        //register org.springframework.beans.factory.support.DefaultListableBeanFactory
		for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
			postProcessor.postProcessBeanDefinitionRegistry(registry);
		}
	}


```

```java
/**
 * Build and validate a configuration model based on the registry of
 * {@link Configuration} classes.
 */
public void processConfigBeanDefinitions(BeanDefinitionRegistry registry) {
    //BeanDefinitionHolder 是BeanDefinition的包装
   List<BeanDefinitionHolder> configCandidates = new ArrayList<>();
    //获取所有的候选者
   String[] candidateNames = registry.getBeanDefinitionNames();

   for (String beanName : candidateNames) {
       //从defaultListable容器中拿出beandifinition
      BeanDefinition beanDef = registry.getBeanDefinition(beanName);
       // ConfigurationClassUtils.checkConfigurationClassCandidate 见下方代码
       //如果满足条件的话。那么会加入到configCandidates容器中
      if (ConfigurationClassUtils.checkConfigurationClassCandidate(beanDef, this.metadataReaderFactory)) {
         configCandidates.add(new BeanDefinitionHolder(beanDef, beanName));
      }
   }

   // Detect any custom bean name generation strategy supplied through the enclosing application context
   SingletonBeanRegistry sbr = null;

   // Parse each @Configuration class
    //实例化了一个配置类解析器
   ConfigurationClassParser parser = new ConfigurationClassParser(
         this.metadataReaderFactory, this.problemReporter, this.environment,
         this.resourceLoader, this.componentScanBeanNameGenerator, registry);

    //候选者
   Set<BeanDefinitionHolder> candidates = new LinkedHashSet<>(configCandidates);
    //已经解析过的集合
   Set<ConfigurationClass> alreadyParsed = new HashSet<>(configCandidates.size());
   do {
      parser.parse(candidates);
      parser.validate();

      Set<ConfigurationClass> configClasses = new LinkedHashSet<>(parser.getConfigurationClasses());
      configClasses.removeAll(alreadyParsed);

      // Read the model and create bean definitions based on its content
      if (this.reader == null) {
         this.reader = new ConfigurationClassBeanDefinitionReader(
               registry, this.sourceExtractor, this.resourceLoader, this.environment,
               this.importBeanNameGenerator, parser.getImportRegistry());
      }
       //1。加载配置类 中带有@bean注解的方法。并把bean注册到register中
       //2。从@ImportSource里面加载 bean信息
       //3。加载@Import 。会调用这个类的实现类 org.springframework.context.annotation.ImportBeanDefinitionRegistrar#registerBeanDefinitions(org.springframework.core.type.AnnotationMetadata, org.springframework.beans.factory.support.BeanDefinitionRegistry, org.springframework.beans.factory.support.BeanNameGenerator)
       // 可参考 com.example.demo.MyImportRegister的实现
      this.reader.loadBeanDefinitions(configClasses);
    //后续非必要代码，已忽略
}



	
	public static boolean checkConfigurationClassCandidate(
			BeanDefinition beanDef, MetadataReaderFactory metadataReaderFactory) {
		//篇幅较长，删掉了多余的代码 
		String className = beanDef.getBeanClassName();
		if (className == null || beanDef.getFactoryMethodName() != null) {
			return false;
		}

		AnnotationMetadata metadata;
		if (beanDef instanceof AnnotatedBeanDefinition &&
				className.equals(((AnnotatedBeanDefinition) beanDef).getMetadata().getClassName())) {
            // 这个条件没太看懂。不过应该是为了找到相等的数据
			// Can reuse the pre-parsed metadata from the given BeanDefinition...
			metadata = ((AnnotatedBeanDefinition) beanDef).getMetadata();
		}
        //随后获取类的Configuration注解
        //因为在启动类上面加上了@SpringBootApplication ，而@SpringBootApplication注解中又用 @AliasFor(annotation = Configuration.class)
        //当前走到这一步，结果肯定是返回true
		Map<String, Object> config = metadata.getAnnotationAttributes(Configuration.class.getName());
		if (config != null && !Boolean.FALSE.equals(config.get("proxyBeanMethods"))) {
			beanDef.setAttribute(CONFIGURATION_CLASS_ATTRIBUTE, CONFIGURATION_CLASS_FULL);
		}
		else if (config != null || isConfigurationCandidate(metadata)) {
			beanDef.setAttribute(CONFIGURATION_CLASS_ATTRIBUTE, CONFIGURATION_CLASS_LITE);
		}
		return true;
	}
```



```java
	private static final Predicate<String> DEFAULT_EXCLUSION_FILTER = className ->
			(className.startsWith("java.lang.annotation.") || className.startsWith("org.springframework.stereotype."));

	public void parse(Set<BeanDefinitionHolder> configCandidates) {
		for (BeanDefinitionHolder holder : configCandidates) {
			BeanDefinition bd = holder.getBeanDefinition();
			parse(((AnnotatedBeanDefinition) bd).getMetadata(), holder.getBeanName());
		}
		this.deferredImportSelectorHandler.process();
	}

	protected final void parse(AnnotationMetadata metadata, String beanName) throws IOException {
		processConfigurationClass(new ConfigurationClass(metadata, beanName), DEFAULT_EXCLUSION_FILTER);
	}


    protected void processConfigurationClass(ConfigurationClass configClass, Predicate<String> filter) throws IOException {
        //肯定不会跳过，本来就一个元素 跳过的话。 那就结束 了
        if (this.conditionEvaluator.shouldSkip(configClass.getMetadata(), ConfigurationPhase.PARSE_CONFIGURATION)) {
            return;
        }
		//this.configurationClasses 为空
        ConfigurationClass existingClass = this.configurationClasses.get(configClass);
        if (existingClass != null) {
            if (configClass.isImported()) {
                if (existingClass.isImported()) {
                    existingClass.mergeImportedBy(configClass);
                }
                // Otherwise ignore new imported config class; existing non-imported class overrides it.
                return;
            }
            else {
                // Explicit bean definition found, probably replacing an import.
                // Let's remove the old one and go with the new one.
                this.configurationClasses.remove(configClass);
                this.knownSuperclasses.values().removeIf(configClass::equals);
            }
        }

        // Recursively process the configuration class and its superclass hierarchy. 递归处理配置类和他的父类 可能还有父类的父类 如此
        //configClass  主函数启动类   filter 就是 DEFAULT_EXCLUSION_FILTER
        // sourceClass 实际上还是 主函数
        SourceClass sourceClass = asSourceClass(configClass, filter);
        // do while 是不管while条件如何。都会执行一次中间的代码块 
        do {
            //重要的是这个方法
            sourceClass = doProcessConfigurationClass(configClass, sourceClass, filter);
        }
        while (sourceClass != null);
		//由于是递归调用  所以com.example.demo.MyBeanDefinitionRegistryPostProcessor 会比main函数先加入到this.configurationClasses中
        this.configurationClasses.put(configClass, configClass);
    }

	//处理配置类
    protected final SourceClass doProcessConfigurationClass(
        ConfigurationClass configClass, SourceClass sourceClass, Predicate<String> filter)
        throws IOException {

        if (configClass.getMetadata().isAnnotated(Component.class.getName())) {
            // Recursively process any member (nested) classes first  首先递归处理任何成员（嵌套）类
            processMemberClasses(configClass, sourceClass, filter);
        }

        // Process any @PropertySource annotations 处理@PropertySource注解。主函数没有 。
        for (AnnotationAttributes propertySource : AnnotationConfigUtils.attributesForRepeatable(
            sourceClass.getMetadata(), PropertySources.class,
            org.springframework.context.annotation.PropertySource.class)) {
            if (this.environment instanceof ConfigurableEnvironment) {
                processPropertySource(propertySource);
            }
            else {
                logger.info("Ignoring @PropertySource annotation on [" + sourceClass.getMetadata().getClassName() +
                            "]. Reason: Environment must implement ConfigurableEnvironment");
            }
        }

        // Process any @ComponentScan annotations  处理@ComponentScan注解。@SpringBootApplication注解中 有 @AliasFor指向 @ComponentScan
        Set<AnnotationAttributes> componentScans = AnnotationConfigUtils.attributesForRepeatable(
            sourceClass.getMetadata(), ComponentScans.class, ComponentScan.class);
        if (!componentScans.isEmpty() &&
            !this.conditionEvaluator.shouldSkip(sourceClass.getMetadata(), ConfigurationPhase.REGISTER_BEAN)) {
            for (AnnotationAttributes componentScan : componentScans) {
                // The config class is annotated with @ComponentScan -> perform the scan immediately
                //如果是@ComponentScan 就立刻执行扫描
                Set<BeanDefinitionHolder> scannedBeanDefinitions =
                    this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName());
                // Check the set of scanned definitions for any further config classes and parse recursively if needed
                //扫描到的beandifinition 会递归的调用parse方法进行解析   //这里扫描出来的结果只有这一个 com.example.demo.MyBeanDefinitionRegistryPostProcessor
                for (BeanDefinitionHolder holder : scannedBeanDefinitions) {
                    BeanDefinition bdCand = holder.getBeanDefinition().getOriginatingBeanDefinition();
                    if (bdCand == null) {
                        bdCand = holder.getBeanDefinition();
                    }
                    if (ConfigurationClassUtils.checkConfigurationClassCandidate(bdCand, this.metadataReaderFactory)) {
                        parse(bdCand.getBeanClassName(), holder.getBeanName());
                    }
                }
            }
        }
		//当递归调用时  MyBeanDefinitionRegistryPostProcessor 方法会率先进入到这里
        // Process any @Import annotations   处理imports  这个方法里面也会递归 。 因为可能在处理@Import注解的时候 这个类里面可能也有spring的注解（比如@bean) ,也有可能@Import  还有@Import注解...
        processImports(configClass, sourceClass, getImports(sourceClass), filter, true);

        // Process any @ImportResource annotations
        //解析 @ImportResource
        AnnotationAttributes importResource =
            AnnotationConfigUtils.attributesFor(sourceClass.getMetadata(), ImportResource.class);
        if (importResource != null) {
            String[] resources = importResource.getStringArray("locations");
            Class<? extends BeanDefinitionReader> readerClass = importResource.getClass("reader");
            for (String resource : resources) {
                String resolvedResource = this.environment.resolveRequiredPlaceholders(resource);
                configClass.addImportedResource(resolvedResource, readerClass);
            }
        }

        // Process individual @Bean methods
        //取到这个class 里面的@Bean注解
        Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(sourceClass);
        for (MethodMetadata methodMetadata : beanMethods) {
            configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
        }

        // Process default methods on interfaces 找到class对应的接口。TODO
        processInterfaces(configClass, sourceClass);

        // Process superclass, if any
        //处理父类
        if (sourceClass.getMetadata().hasSuperClass()) {
            String superclass = sourceClass.getMetadata().getSuperClassName();
            if (superclass != null && !superclass.startsWith("java") &&
                !this.knownSuperclasses.containsKey(superclass)) {
                this.knownSuperclasses.put(superclass, configClass);
                // Superclass found, return its annotation metadata and recurse
                return sourceClass.getSuperClass();
            }
        }

        // No superclass -> processing is complete
        return null;
    }


    private void processMemberClasses(ConfigurationClass configClass, SourceClass sourceClass,
                                      Predicate<String> filter) throws IOException {
		
        Collection<SourceClass> memberClasses = sourceClass.getMemberClasses();
        //如果一个类里面没有任何属性和方法 。那么这个方法就会结束 。主函数没有任何信息  所以直接return了
        if (!memberClasses.isEmpty()) {  
            List<SourceClass> candidates = new ArrayList<>(memberClasses.size());
            for (SourceClass memberClass : memberClasses) {
                if (ConfigurationClassUtils.isConfigurationCandidate(memberClass.getMetadata()) &&
                    !memberClass.getMetadata().getClassName().equals(configClass.getMetadata().getClassName())) {
                    candidates.add(memberClass);
                }
            }
            OrderComparator.sort(candidates);
            for (SourceClass candidate : candidates) {
                if (this.importStack.contains(configClass)) {
                    this.problemReporter.error(new CircularImportProblem(configClass, this.importStack));
                }
                else {
                    this.importStack.push(configClass);
                    try {
                        processConfigurationClass(candidate.asConfigClass(configClass), filter);
                    }
                    finally {
                        this.importStack.pop();
                    }
                }
            }
        }
    }

    public Set<BeanDefinitionHolder> parse(AnnotationAttributes componentScan, final String declaringClass) {
        //最重要的是实例化了一个classpath 扫描器
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(this.registry,
                                                                                    componentScan.getBoolean("useDefaultFilters"), this.environment, this.resourceLoader);

        Class<? extends BeanNameGenerator> generatorClass = componentScan.getClass("nameGenerator");
        boolean useInheritedGenerator = (BeanNameGenerator.class == generatorClass);
        scanner.setBeanNameGenerator(useInheritedGenerator ? this.beanNameGenerator :
                                     BeanUtils.instantiateClass(generatorClass));

        ScopedProxyMode scopedProxyMode = componentScan.getEnum("scopedProxy");
        if (scopedProxyMode != ScopedProxyMode.DEFAULT) {
            scanner.setScopedProxyMode(scopedProxyMode);
        }
        else {
            Class<? extends ScopeMetadataResolver> resolverClass = componentScan.getClass("scopeResolver");
            scanner.setScopeMetadataResolver(BeanUtils.instantiateClass(resolverClass));
        }

        scanner.setResourcePattern(componentScan.getString("resourcePattern"));

        for (AnnotationAttributes filter : componentScan.getAnnotationArray("includeFilters")) {
            for (TypeFilter typeFilter : typeFiltersFor(filter)) {
                scanner.addIncludeFilter(typeFilter);
            }
        }
        for (AnnotationAttributes filter : componentScan.getAnnotationArray("excludeFilters")) {
            for (TypeFilter typeFilter : typeFiltersFor(filter)) {
                scanner.addExcludeFilter(typeFilter);
            }
        }

        boolean lazyInit = componentScan.getBoolean("lazyInit");
        if (lazyInit) {
            scanner.getBeanDefinitionDefaults().setLazyInit(true);
        }

        Set<String> basePackages = new LinkedHashSet<>();
        String[] basePackagesArray = componentScan.getStringArray("basePackages");
        for (String pkg : basePackagesArray) {
            String[] tokenized = StringUtils.tokenizeToStringArray(this.environment.resolvePlaceholders(pkg),
                                                                   ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            Collections.addAll(basePackages, tokenized);
        }
        for (Class<?> clazz : componentScan.getClassArray("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }
        //这一块比较经典。 为什么@SpringBootApplication没配置扫描包。但是spring 依旧能够扫描
        // 如果没有配置的话。默认就是取main class的包  。  最终会去扫描main函数对应的包和子包。
        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(declaringClass));
        }

        scanner.addExcludeFilter(new AbstractTypeHierarchyTraversingFilter(false, false) {
            @Override
            protected boolean matchClassName(String className) {
                return declaringClass.equals(className);
            }
        });
        //执行扫描main函数对应的包和子包。 以及会注册beandifi
        return scanner.doScan(StringUtils.toStringArray(basePackages));
    }

```

