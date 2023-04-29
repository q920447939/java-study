package com.example.nettychatserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.example.nettychatserver")
public class NettChatServerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NettChatServerApplication.class, args);
        ChatServer chatServer = context.getBean(ChatServer.class);
        chatServer.runServe();
    }

}
