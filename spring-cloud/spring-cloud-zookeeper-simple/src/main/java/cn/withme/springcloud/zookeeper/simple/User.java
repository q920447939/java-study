/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月19日
 */
package cn.withme.springcloud.zookeeper.simple;

import org.springframework.stereotype.Component;

/**
 * ClassName: User
 * @Description:
 * @author leegoo
 * @date 2019年06月19日
 */
// 测试Qualifier , Qualifier 可以用来筛选 spring 里面的bean

public class User {
    private int a ;


    public User(int a) {
        this.a = a;
    }


    @Override
    public String toString() {
        return "User{" +this.getClass()+
                '}';
    }
}
