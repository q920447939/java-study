package cn.withmes.nettyechohttp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "cn.withmes.nettyechohttp")
public class NettyEchoHttpApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NettyEchoHttpApplication.class, args);
        NettyEchoServer nettyEchoServer = context.getBean(NettyEchoServer.class);
        nettyEchoServer.startServer();
    }

}
