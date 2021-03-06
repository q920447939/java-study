/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月21日
 */
package cn.withmes.springcloud.open.fegin.client.advance;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ClassName: AdvanceSayingServer
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月21日
 */
//需要和server定义的application.name 一样
@MyFeignClient(name = "${fegin-server}")
public interface AdvanceSayingServer {

    //此处其实就是走的http 协议
    //栗子  /{server.name}:port/say/message?query=query
    @GetMapping("/say/message2")
    String sayMessage2(@RequestParam("message") String message);
}
