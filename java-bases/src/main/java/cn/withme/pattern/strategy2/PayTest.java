/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月20日
 */
package cn.withme.pattern.strategy2;

/**
 * ClassName: PayTest
 * @Description:
 * @author leegoo
 * @date 2020年03月20日
 */
public class PayTest {

    public static void main(String[] args) {
        ChannelPayAbstract channelPayAbstract =new ZhifubaoPay("112");
        Result<ChannelPayAbstract> result = channelPayAbstract.pay();
        System.out.println(result);
    }
}
