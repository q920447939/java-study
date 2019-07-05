/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月03日
 */
package cn.withmes.spring.boot.rabbitmq.producter.simplequeue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: Consumer
 * @Description:
 * @author leegoo
 * @date 2019年07月03日
 */
//@Component //运行放开
public class Consumer {
    private final static String QUEUE_NAME = "test_simple_queue";

    //@PostConstruct
    public  void consumer() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //第二个参数 顺序消费
        channel.queueDeclare(QUEUE_NAME, true, true, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received 共产党说:'" + message + "'");
        };
        System.out.println(2123);
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
        System.out.println("end...");
    }

    //@PostConstruct
    public  void topicTest() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_my_exchange";
        String exchangeType = "topic";
        String routerKey = "user.#";

        //1.声明交换机
        channel.exchangeDeclare(exchangeName, exchangeType);

        //2 声明队列
        //第二个参数 持久化
        //第三个参数  顺序消费
        channel.queueDeclare(QUEUE_NAME, true, true, false, null);

        //3 建立交换机和队列的绑定关系

        channel.queueBind(QUEUE_NAME,exchangeName , routerKey);

        //设置限流
        //服务器提供的最大容量    ,每次接收的最大消息个数  ,是否只针对这个channel 设置
        channel.basicQos(0, 3, false);

       //todo
    }

}
