package cn.withmes.springboot.mybatis.interceptor;

import cn.withmes.springboot.mybatis.interceptor.mapper.UserMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("cn.withmes.springboot.mybatis.interceptor.mapper")
public class SpringBootMybatisInterceptorApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootMybatisInterceptorApplication.class, args);
        UserMapper userMapper = applicationContext.getBean(UserMapper.class);
        userMapper.findAll();
    }

}
