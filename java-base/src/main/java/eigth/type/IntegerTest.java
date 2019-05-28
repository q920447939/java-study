/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月27日
 */
package eigth.type;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: IntegerTest
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月27日
 */
public class IntegerTest {
    public static void main(String[] args) {
        int a = 50;
        Integer b = new Integer(50);
        System.out.println(a==b);
        ConcurrentHashMap c  = new ConcurrentHashMap();
        c.put(1, 1);
        System.out.println(c);
    }
}
