/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月15日
 */
package cn.withmes.spring.boot.dubbo.consume;

/**
 * ClassName: HelloController
 * @Description:
 * @author leegoo
 * @date 2019年05月15日
 */
import cn.withmes.spring.boot.dubbo.provider.HelloService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Reference(check = false,timeout = 10)
    private HelloService helloService;

    @RequestMapping("/hello")
    public String hello() {
        String hello = helloService.sayHello("world");
        System.out.println(helloService.sayHello("SnailClimb"));
        return hello;
    }
}