package cn.withmes.spring.boot.aop;

import cn.withmes.spring.boot.aop.sub.InsertUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootAopApplicationTests {

    @Autowired
    UserService userService;

    @Autowired
    InsertUserService insertUserService;

    static {
        User.userList.add(User.builder().id(2).name("admin").build());
    }

    @Test
    public void contextLoads() {
        userService.findUser(User.userList.get(0));
    }

    @Test
    public void execute() {

        insertUserService.insert();
    }


    @Test
    public void executeException() throws  Exception{
        insertUserService.insertHaveExceoption();
    }




    @Test
    public void beforeAround() {
        userService.findUser(null);
    }


}
