/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月20日
 */
package cn.withme.pattern.strategy2;

/**
 * ClassName: PayAbstract
 * @Description:
 * @author leegoo
 * @date 2020年03月20日
 */
public abstract class ChannelPayAbstract {
    private Pay<ChannelPayAbstract> pay;

    protected abstract String getBankCode ();

    protected  String desc;

    protected abstract boolean check();

    protected Result<ChannelPayAbstract> pay () {
        if (!this.check()) return new Result<ChannelPayAbstract>().fail(this.desc);
        Result<ChannelPayAbstract> pay = this.pay.pay(this);
        return  pay;
    }
}
