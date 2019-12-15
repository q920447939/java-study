/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年12月15日
 */
package cn.withmes.spring.boot.custom;

import cn.withmes.spring.boot.custom.annotation.MyController;
import cn.withmes.spring.boot.custom.annotation.MyResource;

/**
 * ClassName: UserController
 * @Description:
 * @author leegoo
 * @date 2019年12月15日
 */
@MyController
public class UserController {

    @MyResource
    private UserService userService;

    public void getId (String id ) {
        String name = userService.getName(id);
        System.out.println("name:"+name);
    }
}
