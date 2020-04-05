/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月28日
 */
package cn.withme.pattern.strategy4;

/**
 * ClassName: ZhifuBaoPay
 * @Description:
 * @author leegoo
 * @date 2020年03月28日
 */
public class ZhifuBaoPay implements Pay {

    private final String  NAME= "支付宝";

    @Override
    public boolean pay(String orderId) {
        System.out.printf("当前采用[%s]支付\n",NAME);
        return false;
    }
}
