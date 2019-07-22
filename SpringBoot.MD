## **<a id="t1">Spring boot session Redis简单实现</a>**

1. `pom.xml`增加依赖,[[配置地址](spring-boot/spring-boot-single-point/spring-boot-single-point-server01/pom.xml)

   ```xml
           <dependency>
               <groupId>org.springframework.session</groupId>
               <artifactId>spring-session-data-redis</artifactId>
           </dependency>
           <dependency>
               <groupId>org.springframework.session</groupId>
               <artifactId>spring-session-core</artifactId>
           </dependency>
           <dependency>
               <groupId>redis.clients</groupId>
               <artifactId>jedis</artifactId>
               <version>2.9.1</version>
           </dependency>
   ```

2. 增加`redis`配置,[配置地址](spring-boot/spring-boot-single-point/spring-boot-single-point-server01/src/main/resources/application.properties)

   ```properties
   ############################################################
   #
   # REDIS 配置
   #
   ############################################################
   # Redis数据库索引（默认为0）
   spring.redis.database=1
   # Redis服务器地址
   spring.redis.host=localhost
   # Redis服务器连接端口
   spring.redis.port=6379
   # Redis服务器连接密码（默认为空）
   #spring.redis.password=
   
   # 连接超时时间（毫秒）
   spring.redis.timeout=0
   
   server.port=8881
   
   spring.session.store-type=REDIS
   #过期时间
   #spring.session.timeout=60*60*24*2
   # Namespace for keys used to store sessions.  redis key 前缀
   spring.session.redis.namespace=spring:session
   ```

3. 增加`redis pool`配置, [配置地址](spring-boot/spring-boot-single-point/spring-boot-single-point-server01/src/main/java/cn/withmes/spring/boot/single/point/server01/RedisConnectionFactoryConfig.java)

   ```java
   /**
    * @Project:
    * @Author: leegoo
    * @Date: 2019年07月22日
    */
   package cn.withmes.spring.boot.single.point.server01;
   
   import org.springframework.beans.factory.annotation.Value;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.data.redis.connection.RedisPassword;
   import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
   import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
   import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
   import redis.clients.jedis.JedisPoolConfig;
   
   import java.time.Duration;
   
   /**
    * ClassName: RedisConnectionFactory
    *
    * @author leegoo
    * @Description:
    * @date 2019年07月22日
    */
   @Configuration
   public class RedisConnectionFactoryConfig {
   
   
       @Value("${spring.redis.host}")
       private String redisHost;
   
       @Value("${spring.redis.port}")
       private int redisPort;
   
       @Value("${spring.redis.timeout}")
       private int redisTimeout;
   
   //    @Value("${spring.redis.password}")
   //    private String redisAuth;
   
       @Value("${spring.redis.database}")
       private int redisDb;
   
   //    @Value("${spring.redis.jedis.pool.max-active}")
   //    private int maxActive;
   
   //    @Value("${spring.redis.jedis.pool.max-wait}")
   //    private int maxWait;
   
   //    @Value("${spring.redis.jedis.pool.max-idle}")
   //    private int maxIdle;
   
   //    @Value("${spring.redis.jedis.pool.min-idle}")
   //    private int minIdle;
   
       @Bean
       public JedisConnectionFactory connectionFactory() {
           JedisPoolConfig poolConfig = new JedisPoolConfig();
           //poolConfig.setMaxTotal(maxActive);
   //        poolConfig.setMaxIdle(maxIdle);
   //        poolConfig.setMaxWaitMillis(maxWait);
   //        poolConfig.setMinIdle(minIdle);
           poolConfig.setTestOnBorrow(true);
           poolConfig.setTestOnReturn(false);
           poolConfig.setTestWhileIdle(true);
           JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
                   .usePooling().poolConfig(poolConfig).and().readTimeout(Duration.ofMillis(redisTimeout)).build();
   
           // 单点redis
           RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
           // 哨兵redis
           // RedisSentinelConfiguration redisConfig = new RedisSentinelConfiguration();
           // 集群redis
           // RedisClusterConfiguration redisConfig = new RedisClusterConfiguration();
           redisConfig.setHostName(redisHost);
           //redisConfig.setPassword(RedisPassword.of(redisAuth));
           redisConfig.setPort(redisPort);
           redisConfig.setDatabase(redisDb);
   
           return new JedisConnectionFactory(redisConfig, clientConfig);
       }
   
   }
   
   ```

4. 启动类增加配置

   ```java
   package cn.withmes.spring.boot.single.point.server01;
   
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.beans.factory.annotation.Qualifier;
   import org.springframework.beans.factory.annotation.Value;
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.context.annotation.Bean;
   import org.springframework.data.redis.connection.RedisConnectionFactory;
   import org.springframework.data.redis.connection.RedisPassword;
   import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
   import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
   import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
   import org.springframework.data.redis.core.RedisTemplate;
   import org.springframework.data.redis.core.StringRedisTemplate;
   import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RestController;
   import redis.clients.jedis.JedisPoolConfig;
   
   import javax.servlet.Filter;
   import javax.servlet.FilterChain;
   import javax.servlet.ServletException;
   import javax.servlet.ServletRequest;
   import javax.servlet.ServletResponse;
   import javax.servlet.http.HttpServlet;
   import javax.servlet.http.HttpServletRequest;
   import java.io.IOException;
   import java.time.Duration;
   
   @SpringBootApplication
   @EnableRedisHttpSession
   public class SinglePointServer01 {
   
   
       public static void main(String[] args) {
           SpringApplication.run(SinglePointServer01.class, args);
       }
   
   
   
       @RestController
       @RequestMapping("/user")
       public class UserController {
   
           @GetMapping("/token")
           public String saveUserInfoRedis(HttpServletRequest request) {
               request.getSession().setAttribute("xm","this is my userinfo" );
               return "success";
           }
   
           @GetMapping("/token/get")
           public String getUserInfo(HttpServletRequest request) {
               return (String)request.getSession().getAttribute("xm");
           }
       }
   }
   
   ```

5. 通过访问`http://localhost:8881/user/token`,再访问`http://localhost:8881/user/token/get`即可看到设置的session,可到`target`目录下,使用`java -jar projectname --server.port=your port` 执行多份,`redis`中也可以看到新增加的key`spring:session:*`





