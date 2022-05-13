## 总结篇

从7.1 获取`session` 到7.2触发自定义拦截器

主要经过几个步骤


~~~mermaid
flowchart TD
    A[MapperProxyFactory创建代理对象] --> B["调用代理对象(MapperProxy)"]
    B --> C["调用sqlSessionProxy代理对象（SqlSessionInterceptor）"]
    C --> E["获取Session"]
    E-->F["获取Executor"]
    F-->A1["创建StatementHandler"]
    A1-->G["创建代理对象Plugin"]
    G-->H["调用自定义拦截器(ExecutorInterceptor)"]

~~~
