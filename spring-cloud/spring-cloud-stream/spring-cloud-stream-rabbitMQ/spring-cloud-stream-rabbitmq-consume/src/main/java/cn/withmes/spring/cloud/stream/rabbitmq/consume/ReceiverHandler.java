/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月25日
 */
package cn.withmes.spring.cloud.stream.rabbitmq.consume;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * ClassName: ReceiverHnadler
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月25日
 */
@Component
public class ReceiverHandler {
    @Autowired
    ReceiverServer receiverServer;

    @PostConstruct
    public void init() {
        SubscribableChannel channel = receiverServer.receiverMes();
        channel.subscribe(message -> {
            byte[] payload = (byte[]) message.getPayload();
            System.out.println(new String(payload));
        });
    }


}
