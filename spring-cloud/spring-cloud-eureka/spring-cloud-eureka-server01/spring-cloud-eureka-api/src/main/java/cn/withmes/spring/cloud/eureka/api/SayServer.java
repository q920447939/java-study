/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月06日
 */
package cn.withmes.spring.cloud.eureka.api;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * ClassName: SayServer
 * @Description:
 * @author leegoo
 * @date 2019年07月06日
 */
public interface SayServer {

    @GetMapping("/server/say")
    String sayInfo ();
}
