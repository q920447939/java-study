/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月18日
 */
package cn.withme.springcloud.zookeeper.simple;


import org.omg.PortableInterceptor.ClientRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ClassName: LoadBanlance
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月18日
 */
public class LoadBanlance implements ClientHttpRequestInterceptor {

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private RestTemplate restTemplate;


    private Map<String, Set<String>> servers = new ConcurrentHashMap<>();

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        URI uri = httpRequest.getURI();
        //"/" + servierName + "/hello"
        String serverName = uri.getPath().split("/")[1];
        HttpMethod httpRequestMethod = httpRequest.getMethod();
        String  serverMethod = uri.getPath().split("/")[2];
        //获得zookeeper 注册列表(可以传入服务名,这样就能把这个项目所对应的集群节点ip 全部拿到,然后就可以自己模拟负载均衡了)
        Object[] targetServerNames = servers.get(serverName).toArray();
        String targetUrl = (String) targetServerNames[new Random().nextInt(targetServerNames.length)];
        String forObject = restTemplate.getForObject(targetUrl + "/" + serverMethod, String.class);
        return null;
    }


    // 定时任务, cron表达式 一秒钟执行一次
    @Scheduled(cron = "0/1 * * * * ?")
    public void getServerList() {
        List<String> services = client.getServices();
        Map<String, Set<String>> newServerList = new ConcurrentHashMap<>(services.size());
        services.stream().distinct().forEach(e -> {
            List<ServiceInstance> instanceList = client.getInstances(e);
            Set<String> set1 = instanceList.stream().map(
                    m -> m.isSecure() ? "https://" + m.getHost() + ":" + m.getPort()
                            : "http://" + m.getHost() + ":" + m.getPort()
            ).collect(Collectors.toSet());
            newServerList.put(e, set1);
        });
        servers = newServerList;
    }


}
