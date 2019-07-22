package cn.withmes.springcloud.open.fegin.client;

import cn.withmes.springcloud.open.fegin.client.advance.AdvanceSayingServer;
import cn.withmes.springcloud.open.fegin.client.advance.EnableMyFeignC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


//简易版所需要的注解
@SpringBootApplication(scanBasePackages = "cn.withmes.springcloud.open.fegin.client.simple")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "cn.withmes.spring.cloud.open.fegin.server")  //简易版本需要把该注解打开

//自实现版本所需要注解 防止冲突 所以分开
//@SpringBootApplication(scanBasePackages = "cn.withmes.springcloud.open.fegin.client.advance")
//@EnableDiscoveryClient
//@EnableMyFeignC(clients = AdvanceSayingServer.class)  //激活寻找带@MyFeignClient的接口
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

    @Bean
    @LoadBalanced
    public RestTemplate restTemplateBean(){
        return new RestTemplate();
    }

}
