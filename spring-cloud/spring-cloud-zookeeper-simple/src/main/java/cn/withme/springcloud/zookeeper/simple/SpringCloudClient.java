package cn.withme.springcloud.zookeeper.simple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;

@SpringBootApplication(scanBasePackages = "cn.withme.springcloud.zookeeper.simple") //扫描包
@EnableDiscoveryClient //激活服务发现
@EnableScheduling //结果任务调度
public class SpringCloudClient {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudClient.class, args);
    }

    //自定义注入RestTemplate
    //注入一个拦截器  ClientHttpRequestInterceptor 依赖注入  loadBanlanceBean是下面定义的bean的名称
    @Bean
    public RestTemplate restTemplate() {
        //  RestTemplate template = new RestTemplate();
        // template.setInterceptors(Arrays.asList(loadBanlanceBean));
        return new RestTemplate();
    }

    //创建一个拦截器bean ,
    // 因为template注入的时候需要拦截器类(拦截器类可以实现实时读取provider列表,从而实现负载均衡)
    @Bean
    public ClientHttpRequestInterceptor loadBanlanceBean() {
        return new LoadBanlance();
    }


    @Bean
    @CustomLoadBalance
    public RestTemplate loadBalancedTestTemplate() {
        return new RestTemplate();
    }



    @Bean
    @Autowired
    public Object objBean(@CustomLoadBalance Collection<RestTemplate> restTemplates, //通过@CustomLoadBalance 注解(
                          // 其实就是@Qualifier的变种,只是因为@Qualifier的范围太大了,因为当前spring里面有两个RestTemplate ,所以可以过滤掉那一个自定义的RestTemplate),
                          ClientHttpRequestInterceptor loadBanlanceBean) {
        restTemplates.stream().forEach(r -> {
            r.setInterceptors(Arrays.asList(loadBanlanceBean));
        });
        return new Object();
    }



    // 测试Qualifier , Qualifier 可以用来筛选 spring 里面的bean
    @Bean
    @Qualifier("user1")
    public User instance () {
        return new User(1);
    }


    @Bean
    @Qualifier("user2")
    public User instance2 () {
        return new User(2);
    }

}