/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月19日
 */
package cn.withme.springcloud.zookeeper.simple;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: Servers
 * @Description:
 * @author leegoo
 * @date 2019年06月19日
 */
@Component
public class ServerInfo {
    private Map<String, Set<String>> serversMap = new ConcurrentHashMap<>();

    public Map<String, Set<String>> getServersMap() {
        return serversMap;
    }

    public void setServersMap(Map<String, Set<String>> serversMap) {
        this.serversMap = serversMap;
    }
}
