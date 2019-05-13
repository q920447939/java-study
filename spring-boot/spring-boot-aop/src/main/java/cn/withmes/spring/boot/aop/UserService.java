/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月13日
 */
package cn.withmes.spring.boot.aop;

import org.springframework.stereotype.Service;

/**
 * ClassName: UserSevice
 * @Description:
 * @author leegoo
 * @date 2019年05月13日
 */
@Service
public class UserService {

    @MyAopAnno
    public User findUser(User user){
        System.out.println("进入查询");
        //假设查询
        return User.builder().id(2).name("王大锤").build();
    }



}
