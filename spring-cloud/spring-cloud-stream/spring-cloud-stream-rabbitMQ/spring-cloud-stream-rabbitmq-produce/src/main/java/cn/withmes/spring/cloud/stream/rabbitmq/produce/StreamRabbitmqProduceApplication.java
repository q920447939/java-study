package cn.withmes.spring.cloud.stream.rabbitmq.produce;

import cn.withmes.spring.cloud.stream.rabbitmq.produce.simple.SayingServier;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication(scanBasePackages = "cn.withmes.spring.cloud.stream.rabbitmq.produce.simple")
@EnableBinding(SayingServier.class)
public class StreamRabbitmqProduceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamRabbitmqProduceApplication.class, args);
    }

}
