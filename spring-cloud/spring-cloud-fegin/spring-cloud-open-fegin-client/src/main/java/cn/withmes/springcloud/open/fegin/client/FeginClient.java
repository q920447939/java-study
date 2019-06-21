package cn.withmes.springcloud.open.fegin.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(scanBasePackages = "cn.withmes.springcloud.open.fegin.client")
@EnableDiscoveryClient
@EnableFeignClients
public class FeginClient {

    public static void main(String[] args) {
        Map<String, Object> params = new HashMap<>();
        params.put("server.port", 8001);
        params.put("spring.application.name", FeginClient.class.getSimpleName());
//        params.put("management.server.port", 9002);
//        params.put("management.endpoints.web.exposure.include", "*");
        new SpringApplicationBuilder(FeginClient.class)
                .properties(params)
                .run(args);
    }

}
