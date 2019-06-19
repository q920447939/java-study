package cn.withme.springcloud.zookeeper.simple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SpringBootApplication(scanBasePackages = "cn.withme.springcloud.zookeeper.simple")
@EnableDiscoveryClient
@EnableScheduling
public class SpringCloudClient {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudClient.class, args);
    }


    @Autowired
    private LoadBanlance LoadBanlance;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        template.setInterceptors(Arrays.asList(LoadBanlance));
        return template;
    }

    @Bean
    public LoadBanlance getLoadBanlance() {
        return new LoadBanlance();
    }


}