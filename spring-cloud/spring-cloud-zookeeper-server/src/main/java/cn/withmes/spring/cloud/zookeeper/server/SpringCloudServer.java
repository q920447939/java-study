package cn.withmes.spring.cloud.zookeeper.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "cn.withmes.spring.cloud.zookeeper.server")
@EnableDiscoveryClient
public class SpringCloudServer {


    public static void main(String[] args) {
        SpringApplication.run(SpringCloudServer.class, args);
    }

    @RestController
    public class ServerClass {

        @Autowired
        private ServerConfig serverConfig;

        @GetMapping("/hello")
        public String serverHello() {
            return serverConfig.getUrl();
        }
    }
}
