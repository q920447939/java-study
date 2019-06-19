/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月18日
 */
package cn.withme.springcloud.zookeeper.simple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ClassName: Controller
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月18日
 */
@RestController
public class Controller {


    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("invoke/{servierName}/hello")
    public Object getServerHello(@PathVariable String servierName) {
        if (StringUtils.isEmpty(servierName)) {
            return "servierName 为空";
        }
        return restTemplate.getForObject("/" + servierName + "/hello", String.class);
    }


}
