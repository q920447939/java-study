/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月06日
 */
package cn.withmes.spring.cloud.eureka.client01.server;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

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
}
