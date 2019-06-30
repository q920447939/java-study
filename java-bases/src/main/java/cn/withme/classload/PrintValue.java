/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月26日
 */
package cn.withme.classload;

/**
 * ClassName: PrintValue
 * @Description:
 * @author leegoo
 * @date 2019年06月26日
 */

class User {
    public static   int age = 20;

    static {
        System.out.println("this is User class");
    }

    public User() {

    }


}

public class PrintValue {

    public static void main(String[] args) {

    }
}
