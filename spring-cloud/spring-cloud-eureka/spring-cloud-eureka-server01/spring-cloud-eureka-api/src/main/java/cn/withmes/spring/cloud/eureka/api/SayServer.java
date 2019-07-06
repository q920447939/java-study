/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月06日
 */
package cn.withmes.spring.cloud.eureka.api;

import cn.withmes.spring.cloud.eureka.api.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * ClassName: SayServer
 * @Description:
 * @author leegoo
 * @date 2019年07月06日
 */
public interface SayServer {

    @GetMapping("/server/say")
    String sayInfo ();


    @PostMapping("/server/save")
    User save (@RequestBody  User user);
}
