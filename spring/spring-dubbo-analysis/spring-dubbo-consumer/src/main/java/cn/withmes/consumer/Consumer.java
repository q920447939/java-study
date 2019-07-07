/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月07日
 */
package cn.withmes.consumer;

import cn.withmes.provider.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: Consumer
 * @Description:
 * @author leegoo
 * @date 2019年07月07日
 */
public class Consumer {

    @Autowired
    private  DemoService demoService;

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo-demo-consumer.xml");
        context.refresh();
        // Executing remote methods
        DemoService bean = context.getBean(DemoService.class);
        System.out.println("bean:"+bean);
        String hello = bean.sayHello("world");
        // Display the call result
        System.err.println("========"+hello);
    }

    @org.springframework.web.bind.annotation.RestController
    public class RestController {
        @GetMapping("/say")
        public String sayHello (){
            // Executing remote methods
            String hello = demoService.sayHello("world");
            // Display the call result
            System.err.println("========"+hello);
            return hello;
        }
    }








}