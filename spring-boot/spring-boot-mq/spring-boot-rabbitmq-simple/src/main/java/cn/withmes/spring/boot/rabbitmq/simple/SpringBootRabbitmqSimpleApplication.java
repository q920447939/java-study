package cn.withmes.spring.boot.rabbitmq.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cn.withmes.spring.boot.rabbitmq.simple")
public class SpringBootRabbitmqSimpleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRabbitmqSimpleApplication.class, args);
    }

}
