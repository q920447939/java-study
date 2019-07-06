package cn.withmes.spring.cloud.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SpringCloudEurekaServer01Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudEurekaServer01Application.class, args);
    }

}
