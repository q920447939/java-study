/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月20日
 */
package cn.withmes.spring.cloud.hysrix.demo.controller.test;

import cn.withmes.spring.cloud.hysrix.demo.controller.davance.MyHystrixAnnotation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: Test
 * @Description:
 * @author leegoo
 * @date 2019年06月20日
 */
@RestController
public class Test {

    //高阶版本 AOP + annotation
    @GetMapping("/test")
    public String test() throws InterruptedException {
        System.out.println("执行了test()");
        return "返回test()";
    }
}
