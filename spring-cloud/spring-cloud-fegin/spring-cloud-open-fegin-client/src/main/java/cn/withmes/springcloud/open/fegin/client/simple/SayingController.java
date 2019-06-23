/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月21日
 */
package cn.withmes.springcloud.open.fegin.client.simple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: SayingController
 * @Description:
 * @author leegoo
 * @date 2019年06月21日
 */
@RestController
public class SayingController {
/*
    @Autowired
    private SayingServer sayingServer;

    @GetMapping("/feign/client/say")
    public  String sendMessage(@RequestParam(value = "message",required=false) String message){
        return sayingServer.sayMessage(message);
    }*/

}
