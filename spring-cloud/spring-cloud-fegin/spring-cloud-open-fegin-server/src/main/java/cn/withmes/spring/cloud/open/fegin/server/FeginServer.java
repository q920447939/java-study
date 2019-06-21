package cn.withmes.spring.cloud.open.fegin.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(scanBasePackages = "cn.withmes.spring.cloud.open.fegin.server")
@EnableDiscoveryClient
public class FeginServer {

    public static void main(String[] args) {
        Map<String, Object> params = new HashMap<>();
        params.put("server.port", 9001);
        params.put("spring.application.name", "fegin-server");
        new SpringApplicationBuilder(FeginServer.class)
                .properties(params)
                .run(args);
    }

}
