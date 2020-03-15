/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月14日
 */
package cn.withmes.springboot.my.aop.aop.service;

import cn.withmes.springboot.my.aop.aop.exception.MyException;

/**
 * ClassName: UserService
 * @Description:
 * @author leegoo
 * @date 2020年03月14日
 */
public interface UserService {
    User findUser(Integer id) throws MyException;
}
