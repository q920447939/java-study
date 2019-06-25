/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月25日
 */
package cn.withmes.spring.cloud.stream.rabbitmq.produce.simple;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: SimpleController
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月25日
 */
@RestController
public class SimpleController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SayingServier sayingServier;

    @GetMapping("/simple/send")
    public void send (@RequestParam String message) {
        rabbitTemplate.convertAndSend(message );
    }



    @GetMapping("/simple/send2")
    public boolean send2 (@RequestParam String message) {
        MessageChannel messageChannel = sayingServier.xiaoming();
        return messageChannel.send(new GenericMessage<>("hello .this is send2 method."));
    }
}
