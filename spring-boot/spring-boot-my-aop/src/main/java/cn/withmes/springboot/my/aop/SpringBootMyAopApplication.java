package cn.withmes.springboot.my.aop;

import cn.withmes.springboot.my.aop.exception.MyException;
import cn.withmes.springboot.my.aop.service.User;
import cn.withmes.springboot.my.aop.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import cn.withmes.springboot.my.aop.customer.bean.CustomerScan;

@SpringBootApplication
//@CustomerScan(basePackages = "cn.withmes.springboot.my.aop.customer")
public class SpringBootMyAopApplication {

    public static void main(String[] args) throws MyException, InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootMyAopApplication.class, args);

      /*  UserService userService = (UserService) context.getBean("userServiceImpl");
        User user1 = userService.findUser(1);
        System.out.println("user1 ="+user1);

        //模拟一个正常调用
        //User user2 = userService.findUser(2);
        //System.out.println("user2 ="+user2);
        //第三次调用
        //users 中已经没有了,而且不应该调用rollBack方法
        User user3 = userService.findUser(1);
        System.out.println("user3 ="+user3);
        System.out.println();*/
    }

}
