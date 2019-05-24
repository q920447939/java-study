package cn.withmes.springboot.mybatis.interceptor;

import cn.withmes.springboot.mybatis.interceptor.mapper.UserMapper;
import cn.withmes.springboot.mybatis.interceptor.pojo.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootMybatisInterceptorApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private UserMapper userMapper;


    @Test
    @Rollback
    public void findByName() throws Exception {
        User user = new User();
        user.setAge(10);
        user.setStartPage(50);
        user.setEndPage(100);
        userMapper.findByName(user);
    }




}
