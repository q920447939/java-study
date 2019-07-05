package cn.withmes.spring.boot.rabbitmq.simple;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRabbitmqSimpleApplicationTests {


    @Autowired
    private RabbitTemplate  rabbitTemplate;


    @Test
    public void contextLoads() {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("name", "毛泽东");
        Message message = new Message("这是Body ,</br> 红军长征两万五千里".getBytes(), messageProperties);
        rabbitTemplate.convertAndSend("queue01", "rabbit.mq", message, (message1) -> {
            System.err.println("修改额外的head");
            message1.getMessageProperties().getHeaders().put("name", "西金品");
            return message1;
        });
        rabbitTemplate.convertAndSend("queue01", "rabbit.mq","这是一条无名的消息");

    }

}
