/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月03日
 */
package cn.withmes.spring.boot.rabbitmq.producter.simplequeue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: SimpleController
 *
 * @author leegoo
 * @Description:
 * @date 2019年07月03日
 */
@RestController
public class SimpleController {

    /**
     * 队列名称
     */
    private static final String QUEUE = "test_simple_queue";

    static Connection con = null;


    @GetMapping("/send")
    public static void send(String message) {
        Channel channel = null;
        try {
            // 获取连接
            if (null == con) {
                con = getConnection();
            }
            // 从连接中创建通道
            channel = con.createChannel();
            // 声明一个队列
            channel.queueDeclare(QUEUE, false, false, false, null);
            // 发送消息
            channel.basicPublish("", QUEUE, null, message.getBytes());
            System.out.println("send success");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接
            //close(channel, con);
        }
    }


    /**
     * 获取连接
     *
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public static Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        // 设置服务地址
        factory.setHost("127.0.0.1");
        // 端口
        factory.setPort(5672);
        // vhost
        factory.setVirtualHost("/");
        // 用户名
        factory.setUsername("guest");
        // 密码
        factory.setPassword("guest");
        return factory.newConnection();
    }


    /**
     * 关闭连接
     *
     * @param channel
     * @param con
     */

    public static void close(Channel channel, Connection con) {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


}
