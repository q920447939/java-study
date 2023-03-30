package com.example.nettychatserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.nettychatserver")
public class NettChatServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettChatServerApplication.class, args);
    }

}
