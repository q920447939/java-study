package cn.withmes.spring.cloud.eureka.client01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class SpringCloudEurekaClient01Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudEurekaClient01Application.class, args);
    }


    @Bean
    public RestTemplate restTemplate  () {
        return  new RestTemplate();
    }
}
