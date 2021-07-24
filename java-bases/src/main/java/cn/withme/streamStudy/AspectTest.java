/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年07月23日
 */
package cn.withme.streamStudy;

import org.junit.Test;

/**
 * ClassName: AspectTest
 * @Description:
 * @author leegoo
 * @date 2021年07月23日
 */
public class AspectTest {


    @Test
    public void test () throws Exception {
        IOrder newInstance = AspectFactory.newInstance(new Order());
        newInstance.doPay("5");
    }
}
