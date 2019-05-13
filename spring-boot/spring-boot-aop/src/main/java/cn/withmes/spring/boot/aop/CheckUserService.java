/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月13日
 */
package cn.withmes.spring.boot.aop;

import org.springframework.stereotype.Service;

/**
 * ClassName: CheckUserService
 * @Description:
 * @author leegoo
 * @date 2019年05月13日
 */
@Service
public class CheckUserService {

    public Boolean checkUser(){
        User user = User.userList.get(0);
        if (null == user || !"admin".equals(user.getName())) {
            System.out.println("不是管理员,没有资格进入");
            return false;
        }
        System.out.println("权限验证成功");
        return true;
    }
}
