/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年12月15日
 */
package cn.withmes.spring.boot.custom;

import cn.withmes.spring.boot.custom.annotation.MyService;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: UserServiceImpl
 * @Description:
 * @author leegoo
 * @date 2019年12月15日
 */
@MyService
public class UserServiceImpl implements  UserService {

    private static Map<String,String> userMap = new HashMap<>();

    static {
        userMap.put("1", "张三");
        userMap.put("2", "李四");
        userMap.put("3", "王五");
    }

    @Override
    public String getName(String id) {
        return null == id  || !userMap.containsKey(id) ? "没有此用户":
                userMap.get(id) ;
    }
}
