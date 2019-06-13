/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月28日
 */
package cn.withme.jmc;

import java.util.Random;

/**
 * ClassName: JavaMissionControl
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月28日
 */
public class JavaMissionControl {

    public static void main(String[] args) throws Exception {
        while (true) {
            if (new Random().nextInt() > 0) {
                System.out.println("the number greater than zero");
            }
            Thread.sleep(2_000);
        }
    }
}
