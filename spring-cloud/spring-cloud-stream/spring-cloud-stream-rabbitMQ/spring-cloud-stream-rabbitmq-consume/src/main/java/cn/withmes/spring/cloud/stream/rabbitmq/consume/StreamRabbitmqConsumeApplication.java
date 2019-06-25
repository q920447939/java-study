package cn.withmes.spring.cloud.stream.rabbitmq.consume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication(scanBasePackages = "cn.withmes.spring.cloud.stream.rabbitmq.consume")
@EnableBinding(ReceiverServer.class)
public class StreamRabbitmqConsumeApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamRabbitmqConsumeApplication.class, args);
    }

}
