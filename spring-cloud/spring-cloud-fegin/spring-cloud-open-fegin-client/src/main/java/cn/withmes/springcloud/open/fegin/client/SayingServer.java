/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月21日
 */
package cn.withmes.springcloud.open.fegin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ClassName: SayingServer
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月21日
 */
@FeignClient(value = "fegin-server")
public interface SayingServer {

    @GetMapping("/say/message")
    String sayMessage(@RequestParam("message") String message);
}
