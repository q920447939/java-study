1,**启动spring-cloud-eureka-server01** 

​	1.1在启动参数里面增加配置使用`peer1`   

```java
--spring.profiles.active=peer1
```

   1.2 [启动spring-cloud-eureka-server01](spring-cloud-eureka-server01/src/main/java/cn/withmes/spring/cloud/eureka/server01/SpringCloudEurekaServer01Application.java)





2,**启动spring-cloud-eureka-server02** 
​	2.1在启动参数里面增加配置使用`peer2`   

```java
--spring.profiles.active=peer2
```
​    2.2[启动spring-cloud-eureka-server02](spring-cloud-eureka-server02/src/main/java/cn/withmes/spring/cloud/eureka/server02/SpringCloudEurekaServer02Application.java)   





3,**spring-cloud-eureka-client01** 
      [spring-cloud-eureka-client01](spring-cloud-eureka-client01/src/main/java/cn/withmes/spring/cloud/eureka/client01/SpringCloudEurekaClient01Application.java)   