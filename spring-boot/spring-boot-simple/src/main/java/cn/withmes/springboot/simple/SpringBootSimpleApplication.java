package cn.withmes.springboot.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class SpringBootSimpleApplication {

    public static void main(String[] args) {
        ClassLoader classLoader = SpringBootSimpleApplication.class.getSuperclass().getClassLoader();
        ClassLoader parent = SpringBootSimpleApplication.class.getClassLoader().getParent();
        System.err.println("SpringBootSimpleApplication开始运行了,"+  parent.getClass());
        SpringApplication.run(SpringBootSimpleApplication.class, args);
    }

}
