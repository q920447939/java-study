/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月20日
 */
package cn.withmes.spring.cloud.hysrix.controller.davance;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: AdvanceController
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月20日
 */
@RestController
@RequestMapping("/advance")
public class AdvanceController {

    //高阶版本 AOP
    @GetMapping("say")
    public String sayMethod(@RequestParam(name = "time") long time) throws InterruptedException {
        Thread.sleep(time);
        return "执行了" + time + "秒....";
    }


    //高阶版本 AOP + annotation
    @MyHystrixAnnotation(timeout = 300)
    @GetMapping("/anno/say")
    public String sayMethodAnnotation(@RequestParam(name = "time") long time) throws InterruptedException {
        Thread.sleep(time);
        return "执行了" + time + "秒....";
    }

}
