
通过`main`函数上面的注解`@MapperScan("cn.withmes.springboot.mybatis.interceptor")`

`@MapperScan`注解上标记了`@Import(MapperScannerRegistrar.class)`

所以在加载`BeanDefinition`的时候，会去调用`org.mybatis.spring.annotation.MapperScannerRegistrar.registerBeanDefinitions(org.springframework.core.type.AnnotationMetadata, org.springframework.beans.factory.support.BeanDefinitionRegistry)`方法

在`MapperScannerRegistrar`中，自定了一个扫描器。

当扫描到`BeanDefinition`后，会调用
`org.mybatis.spring.mapper.ClassPathMapperScanner.processBeanDefinitions` 方法

**设置`  definition.setBeanClass(this.mapperFactoryBeanClass);`** 

这个`this.mapperFactoryBeanClass`默认就是`MapperFactoryBean.class`
 
