/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月25日
 */
package cn.withmes.spring.cloud.stream.rabbitmq.consume;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * ClassName: ReceiverServer
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月25日
 */
public interface ReceiverServer {

    @Input(value = "xiaomingChannel2019")
    SubscribableChannel receiverMes();

}
