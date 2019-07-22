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
