package cn.withmes.springboot.my.aop;

import cn.withmes.springboot.my.aop.customer.service.UserService;
import cn.withmes.springboot.my.aop.service.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class SpringBootMyAopApplicationTests {

    @Resource(name = "userServiceImpl")
    private UserService  userService;

    @Resource(name = "lsUser")
    private UserService  lsUser;

    @Test
    void testCustomerAnnotation() {
        User user = userService.findUser(1);
        System.out.println("user:"+user);
        User ls = lsUser.findUser(2);
        System.out.println("ls:"+ls);
    }

}
