/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月06日
 */
package cn.withmes.spring.cloud.eureka.client01.server;

import cn.withmes.spring.cloud.eureka.api.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ClassName: ClientSayServer
 *
 * @author leegoo
 * @Description:
 * @date 2019年07月06日
 */

@FeignClient(name = "spring-cloud-eureka-server")
public interface ClientSayServer {

    @GetMapping("/server/say")
    String sayinfo();

    @RequestMapping(value = "/server/save",method = RequestMethod.POST)
    User save(@RequestBody User user);

    @GetMapping("/server/getMessageById")
    String getMessageById(@RequestParam("id") String id);
}
