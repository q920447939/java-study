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

    @PostMapping("/save")
    User save(@RequestParam("user") User user);
}
