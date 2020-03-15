/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月14日
 */
package cn.withmes.springboot.my.aop.customer.service;

import cn.withmes.springboot.my.aop.customer.bean.MyService;
import cn.withmes.springboot.my.aop.service.Data;
import cn.withmes.springboot.my.aop.service.User;

import javax.annotation.Resource;

/**
 * ClassName: UserServiceImpl
 *
 * @author leegoo
 * @Description:
 * @date 2020年03月14日
 */

@MyService(name = "lsUser") // 使用自定义注解,注入spring容器
public class UserServiceImpl2 implements UserService {

    @Resource
    private Data data;

    @Override
    public User findUser(Integer id)  {
        return data.users.get(id);
    }

}
