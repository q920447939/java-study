package com.example.nettychatclient;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.example.nettychatclient")
public class NettyChatClientApplication {

    @SneakyThrows
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(NettyChatClientApplication.class, args);
        Thread thread = new Thread(() -> {
            CommandHandler commandHandler = applicationContext.getBean(CommandHandler.class);
            commandHandler.run();
        },"commandThread");
        thread.start();
        Thread.sleep(Integer.MAX_VALUE);
    }

}
