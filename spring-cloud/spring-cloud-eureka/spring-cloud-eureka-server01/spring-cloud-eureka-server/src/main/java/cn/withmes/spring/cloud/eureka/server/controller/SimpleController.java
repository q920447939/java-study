/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月06日
 */
package cn.withmes.spring.cloud.eureka.server.controller;

import cn.withmes.spring.cloud.eureka.api.UserService;
import cn.withmes.spring.cloud.eureka.api.pojo.User;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: SimpleController
 * @Description:
 * @author leegoo
 * @date 2019年07月06日
 */
@RestController
public class SimpleController implements UserService {


    @Override
    public User getUserById() {
        return null;
    }
}
