/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月28日
 */
package cn.withme.pattern.strategy4;

/**
 * ClassName: WeiXinPay
 * @Description:
 * @author leegoo
 * @date 2020年03月28日
 */
public class WeiXinPay implements Pay {

    private final String  NAME= "微信";

    @Override
    public boolean pay(String orderId) {
        System.out.printf("当前采用[%s]支付\n",NAME);
        return false;
    }
}