/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月19日
 */
package cn.withme.springcloud.zookeeper.simple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ClassName: Task
 * @Description: 定时获取zookeeper 服务列表
 * @author leegoo
 * @date 2019年06月19日
 */
@Component
public class Task {

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private ServerInfo serverInfo;

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
        serverInfo.setServersMap(newServerList);
    }
}
