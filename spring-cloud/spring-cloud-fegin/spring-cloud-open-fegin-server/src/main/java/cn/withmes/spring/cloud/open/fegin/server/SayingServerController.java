/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月21日
 */
package cn.withmes.spring.cloud.open.fegin.server;

import cn.withmes.springcloud.open.fegin.client.SayingServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: SayingServerIml
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月21日
 */
@RestController
public class SayingServerController implements SayingServer {

    @GetMapping("/say/message")
    @Override
    public String sayMessage(@RequestParam(value = "message",required=false) String message) {
        return "hello ,</br> this is server,</br> client message is "+message;
    }
}
