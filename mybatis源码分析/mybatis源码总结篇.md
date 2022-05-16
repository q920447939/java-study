## 总结篇

~~~mermaid
flowchart TD
	direction TB
	subgraph dao初始化
	direction TB
    A["通过注解@MapperScan扫描对应的DAO接口"] --> B["给特定的Beandinition设置FactoryBean(MapperFactoryBean)"]
    B --> C["调用sqlSessionProxy代理对象（SqlSessionInterceptor）"]
     end
    direction TB
    subgraph 获取配置文件
        direction TB
        i2["mybatis.jar包通过与spring boot集合。"] -->f2["处理META-INF/spring.factories"]-->f3["实例化sqlTemplate、sqlSessionFactory、DataSource"]
    end
    C-->D["实现InitializingBean，调用初始化方法"]
    f3-->D
    D-->D1['将mapper放到knownMappers中']
    D1-->D2['创建MapperProxy代理对象']
    D2-->E1["手动调用findByName(自己写的mapper)"]
    E1-->E2["获取session"]
    E2-->E3["创建Plugin代理对象"]-->E4["调用自定义拦截器（ExecutorInterceptor）"]
    subgraph 底层查询与结果处理
    direction TB
        F1["通过Socket调用Mysql"]-->F2["处理结果集"]
    end
    E4-->底层查询与结果处理
    F2-->END["结束"]
    
~~~

