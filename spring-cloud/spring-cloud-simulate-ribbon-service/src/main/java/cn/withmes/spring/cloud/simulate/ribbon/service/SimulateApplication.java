/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月18日
 */
package cn.withmes.spring.cloud.simulate.ribbon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName: SimulateApplication
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月18日
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling // 激活定时任务
public class SimulateApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SimulateApplication.class).run(args);
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    @RestController
    static class Inclass {
        @Autowired
        private DiscoveryClient client;


        @GetMapping("getinstance")
        public List<String> instanceAll() {
            //获得zookeeper 注册列表(可以传入服务名,这样就能把这个项目所对应的集群节点ip 全部拿到,然后就可以自己模拟负载均衡了)
            List<String> services = client.getServices();
            return services;

        }

        private AtomicInteger integer = new AtomicInteger(1);

        // 定时任务, cron表达式 一秒钟执行一次
        @Scheduled(cron = "0/1 * * * * ?")
        public void testCron() {
            System.out.println("this value is :" + integer.incrementAndGet());
        }

    }

}
