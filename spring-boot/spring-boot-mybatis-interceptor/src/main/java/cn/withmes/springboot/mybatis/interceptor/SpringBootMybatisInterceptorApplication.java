package cn.withmes.springboot.mybatis.interceptor;

import cn.withmes.springboot.mybatis.interceptor.pojo.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootMybatisInterceptorApplication {

    public static void main(String[] args) {
        User user = new User();


        SpringApplication.run(SpringBootMybatisInterceptorApplication.class, args);
    }

}
