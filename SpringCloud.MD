## **<a id="t1">SpringCloud集成 Eureka +Feign+ Hystrix</a>**
1. **项目地址**
   - [client](spring-cloud/spring-cloud-eureka/spring-cloud-eureka-client01/src/main/java/cn/withmes/spring/cloud/eureka/client01/SpringCloudEurekaClient01Application.java)
   
   - [server01](spring-cloud/spring-cloud-eureka/spring-cloud-eureka-server01/spring-cloud-eureka-server/src/main/java/cn/withmes/spring/cloud/eureka/server/SpringCloudEurekaServer01Application.java)
   
   - [server02](spring-cloud/spring-cloud-eureka/spring-cloud-eureka-server02/spring-cloud-eureka-server/src/main/java/cn/withmes/spring/cloud/eureka/server/SpringCloudEurekaServer02Application.java)



2. **配置**

   - client

     - `application.yml`增加配置   [地址](spring-cloud/spring-cloud-eureka/spring-cloud-eureka-client01/src/main/resources/application.yml)

       ```yaml
       feign:
         hystrix:
           enabled: true
       ```

     - 启动类增加注解启动`Hystrix`  [地址](spring-cloud/spring-cloud-eureka/spring-cloud-eureka-client01/src/main/java/cn/withmes/spring/cloud/eureka/client01/SpringCloudEurekaClient01Application.java)

       ```java
       @EnableHystrix //激活Hystrix
       ```

     - 在client server类上面增加注解 [地址](spring-cloud/spring-cloud-eureka/spring-cloud-eureka-client01/src/main/java/cn/withmes/spring/cloud/eureka/server/ClientSayServer.java)

       ```java
       @FeignClient(name = "spring-cloud-eureka-server",fallback = FallBackDefault.class)
       ```

     - 实现熔断方法,比如我上方指定的熔断方法是`FallBackDefault.class`  [地址](spring-cloud/spring-cloud-eureka/spring-cloud-eureka-client01/src/main/java/cn/withmes/spring/cloud/eureka/server/FallBackDefault.java)

       ```java
       @Component  //注意注解,增加到spring 容器
       public class FallBackDefault implements   ClientSayServer{
           @Override
           public String sayinfo() {
               return "熔断了.当前方法:{sayinfo}";
           }
       
           @Override
           public User save(User user) {
               return null;
           }
       
           @Override
           public String getMessageById(String id) {
               return "熔断了.当前方法:{getMessageById}";
           }
       }
       
       ```

   - server

     暂无

   

   3. 访问`http://localhost:8846/fallback/feign?id=sadas`会出现熔断和非熔断的情况

   4. 注意 
      - `feign`默认超时时间是1秒,所以如果需要修改超时时间需要在网上查询配置
      - `application.yml`注册的eureka,我是用的`docker-compose`,所以在`windows`下需要改一下`eureka`的注册地址

