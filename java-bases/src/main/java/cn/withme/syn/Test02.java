/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月15日
 */
package cn.withme.syn;

/**
 * ClassName: Test02
 * @Description:
 * @author leegoo
 * @date 2019年06月15日
 */
public class Test02 {

    public static void say () {
        synchronized (Test02.class) {
            System.out.println(123);
        }

    }


    public static synchronized void sa2 () {
        System.out.println(12);
    }


    public static void main(String[] args) {

    }
}
