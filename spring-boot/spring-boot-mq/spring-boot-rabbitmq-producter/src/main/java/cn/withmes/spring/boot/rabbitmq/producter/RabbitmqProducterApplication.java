package cn.withmes.spring.boot.rabbitmq.producter;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import com.rabbitmq.client.DeliverCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RabbitmqProducterApplication {


    public static void main(String[] args) throws IOException, TimeoutException {
        ConfigurableApplicationContext run = SpringApplication.run(RabbitmqProducterApplication.class, args);


    }


}

