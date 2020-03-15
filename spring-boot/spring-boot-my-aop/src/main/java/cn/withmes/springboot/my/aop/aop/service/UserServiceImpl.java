/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月14日
 */
package cn.withmes.springboot.my.aop.aop.service;

import cn.withmes.springboot.my.aop.aop.exception.MyException;
import cn.withmes.springboot.my.aop.aop.scan.MyCTransaction;
import cn.withmes.springboot.my.aop.aop.scan.MyTransaction;

import javax.annotation.Resource;

/**
 * ClassName: UserServiceImpl
 *
 * @author leegoo
 * @Description:
 * @date 2020年03月14日
 */

@MyCTransaction // 使用自定义注解,注入spring容器
public class UserServiceImpl implements UserService {

    @Resource
    private Data data;

    @Override
    @MyTransaction(callBackClass = UserServiceImpl.class, callBackMethod = "rollBack")
    public User findUser(Integer id) throws MyException {
        //这里当id =1时,模拟抛出异常,  当抛出异常之后会执行 rollBack() 方法. 然后再次调用查看是否已经删除数据
        if (id == 1 &&  data.users.containsKey(id)){
            MyException exception = new MyException("");
            throw exception;
        }
        return data.users.get(id);
    }


    // 当执行失败后,会调用这个方法回滚
    public void rollBack(Integer id) {
        System.out.println("进入rollBack方法:" + id);
        data.users.remove(id);
    }
}
