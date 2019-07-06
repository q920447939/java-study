/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月06日
 */
package cn.withmes.spring.cloud.eureka.client01.controller;

import cn.withmes.spring.cloud.eureka.api.pojo.User;
import cn.withmes.spring.cloud.eureka.client01.server.ClientSayServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * ClassName: ClientController
 *
 * @author leegoo
 * @Description:
 * @date 2019年07月06日
 */
@RestController
public class ClientController {

    @Autowired
    private ClientSayServer clientSayServer;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/say")
    public String sayinfo () {
        System.err.println("sayinfo.....");
        List<ServiceInstance> instances = discoveryClient.getInstances("spring-cloud-eureka-server");
        String sayinfo = clientSayServer.sayinfo();
        return sayinfo;
    }


    @PostMapping("/save")
    public User save (@RequestBody User user) {
        System.err.println("save.....user:"+user);
        // 必须配置feign 的超时时间,因为server 会睡眠一秒,而feign 默认超时时间是1秒钟 ,如果不配置会导致 Read Time Out
        //
        /*1. application.yml加入
        * #ribbon的超时时间
            feign:
              client:
                config:
                  remote-service:           #服务名，填写default为所有服务
                    connectTimeout: 10000
                    readTimeout: 10000

         2.配置 FeignConfigure.class
        * */
        User user1 = clientSayServer.save(user);
        return user1;
    }

}

