package com.example.springboot.myfactorybean;

import com.example.springboot.myfactorybean.annotation.FruitAnnotation;
import com.example.springboot.myfactorybean.bean.Apple;
import com.example.springboot.myfactorybean.register.FruitRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(FruitRegister.class)
public class SpringBootMyFactoryBeanApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootMyFactoryBeanApplication.class, args);
        Apple fooBean = applicationContext.getBean(Apple.class);

    }

}
