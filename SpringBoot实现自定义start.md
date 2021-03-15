## SpringBoot实现自定义start

实现原理比较简单,主要是依靠META-INF/spring.factories  文件中,k(spring 对应的配置类) 对应的v 自定义的实现类,完成自定义的start

spring.factories 文件中新写如配置

如下

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration = cn.withmes.springbootmystartconfig.DemoConfigSelect
```



DemoConfigSelect类如下

```java
@Import(DemoConfig.class)
public class DemoConfigSelect {

    @Bean
    @ConditionalOnMissingBean(value = DemoConfig.class)
    public DemoConfig demoConfig() {
        return new DemoConfig();
    }
}


```

DemoConfig类如下

```java
@ConfigurationProperties(prefix = "demo")
@Setter
@Getter
public class DemoConfig {
    private String name;

    @Override
    public String toString() {
        return "DemoConfig{" +
                "name='" + name + '\'' +
                '}';
    }
}
```



所有的核心逻辑已经处理完成,那么如果有一个项目依赖这个项目的话,  项目会去application.properties 里面寻找demo.name 属性

然后注入到DemoCofig Bean 中



代码可参考 

https://github.com/q920447939/java-study/tree/master/spring-boot/spring-boot-my-start