/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月20日
 */
package cn.withme.pattern.strategy2;

import org.apache.commons.lang3.StringUtils;

/**
 * ClassName: ZhifubaoPay
 * @Description:
 * @author leegoo
 * @date 2020年03月20日
 */
public class ZhifubaoPay extends  PayBase {

    private String zhifuSecureKey;

    public ZhifubaoPay(String zhifuSecureKey) {
        this.zhifuSecureKey  = zhifuSecureKey;
    }

    @Override
    protected String getBankCode() {
        return BankCodeEnum.ZHIFUBAO.getCode();
    }


    @Override
    protected boolean check() {
        if (!StringUtils.isNotBlank(this.zhifuSecureKey)){
            this.desc = "支付密钥不能为空";
            return false;
        }
        if ( this.zhifuSecureKey.length() != 12){
            this.desc = "支付密钥长度不正确";
            return false;
        }
        return true;
    }
}
