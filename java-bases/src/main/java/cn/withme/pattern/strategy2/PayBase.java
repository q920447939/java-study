/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月20日
 */
package cn.withme.pattern.strategy2;

/**
 * ClassName: PayBase
 *
 * @author leegoo
 * @Description:
 * @date 2020年03月20日
 */
public class PayBase extends ChannelPayAbstract {

    protected String channelCode;

    protected Double amount;

    protected String bankNo;

    protected String bankCode;

    @Override
    protected String getBankCode() {
        throw new RuntimeException();
    }


    @Override
    protected boolean check() {
        return false;
    }


}
