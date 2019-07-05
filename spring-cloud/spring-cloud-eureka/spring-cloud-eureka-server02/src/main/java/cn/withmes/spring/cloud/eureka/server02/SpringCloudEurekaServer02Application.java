package cn.withmes.spring.cloud.eureka.server02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SpringCloudEurekaServer02Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudEurekaServer02Application.class, args);
    }

}
