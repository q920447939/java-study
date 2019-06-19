/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月18日
 */
package cn.withmes.spring.cloud.zookeeper.server;

/**
 * ClassName: ServerConfig
 *
 * @Description:
 * @author leegoo
 * @date 2019年06月18日
 */

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class ServerConfig implements ApplicationListener<WebServerInitializedEvent> {
    private int serverPort;

    public String getUrl() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "当前服务ip地址为:" + address.getHostAddress() + ",当前服务ip为:" + this.serverPort;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
    }

}
