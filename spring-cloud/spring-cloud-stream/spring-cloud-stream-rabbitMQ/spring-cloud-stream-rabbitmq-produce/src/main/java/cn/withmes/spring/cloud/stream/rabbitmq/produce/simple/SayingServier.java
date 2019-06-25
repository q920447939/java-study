/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月25日
 */
package cn.withmes.spring.cloud.stream.rabbitmq.produce.simple;

import org.springframework.amqp.core.Message;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * ClassName: SayingServier
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月25日
 */

public interface SayingServier {

    @Output("xiaomingChannel2019")
    MessageChannel xiaoming();
}
