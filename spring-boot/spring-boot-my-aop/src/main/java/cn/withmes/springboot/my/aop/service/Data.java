/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月14日
 */
package cn.withmes.springboot.my.aop.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: Data
 * @Description:
 * @author leegoo
 * @date 2020年03月14日
 */
@Service
public class Data  implements CommandLineRunner {
    public Map<Integer, User> users = new HashMap<>(

    );


    @Override
    public void run(String... args) throws Exception {
        users.put(1, new User(1, "小红"));
        users.put(2, new User(2, "小明"));
        users.put(3, new User(3, "小三"));
        System.out.println("初始化数据:"+users);
    }

    public User getUsers(Integer id) {
        return users.get(id);
    }

}
