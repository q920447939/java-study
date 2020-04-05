/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月28日
 */
package cn.withme.pattern.strategy4;

/**
 * ClassName: StrategyTest
 * @Description:
 * @author leegoo
 * @date 2020年03月28日
 */
public class StrategyTest {

    public static void main(String[] args) {
        Pay pay = new WeiXinPay();
        boolean b = pay.pay("1");
        System.out.println(b);
    }
}
