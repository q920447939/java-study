/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月20日
 */
package cn.withmes.spring.cloud.hysrix.controller.middle;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: Controller
 *中阶版本
 * @author leegoo
 * @Description:
 * @date 2019年06月20日
 */
@RestController
@RequestMapping("/middle")
public class MiddleController {

    private ExecutorService executorService = Executors.newCachedThreadPool();

    @GetMapping("say")
    public void say(@RequestParam(name = "time") long time) throws TimeoutException, MyException {
        System.err.println("say() begin");
        Future<String> future   = executorService.submit(() ->  dosomething(time) );
        try {
            future.get(200, TimeUnit.MILLISECONDS);
        } catch (InterruptedException  |ExecutionException | TimeoutException e) {
           // e.printStackTrace();
            future.cancel(true);
            throw  new TimeoutException(e.getMessage());
        }
        dosomething(time);
        System.err.println("say() end");
    }

    private String dosomething(long time) throws  MyException {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            //e.printStackTrace();
            throw new MyException("执行超时了....");
        }
        return "执行了" + time + "秒....";
    }
}
