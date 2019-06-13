/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月22日
 */
package cn.withme.syn;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName: Demo01
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月22日
 */
public class Demo01 {

    private volatile static Demo01 instance = null;

    private volatile static AtomicBoolean flg = new AtomicBoolean();

    private static Long t1 = 0L;
    private static Long t2 = 0L;

    private volatile static AtomicInteger i1 = new AtomicInteger(0);

    private static Boolean flg2 = false;

    private Demo01() {

    }




    public static Demo01 getInstance() {

        return instance;
    }


    public static int val(int val) {
        System.out.println(val);
        return val;
    }


    public static void main(String[] args) {
        Demo01.getInstance();
    }

}
