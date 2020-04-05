package cn.withme.pattern.strategy4;
/**
 * ClassName: BankCodeEnum
 * @Description:
 * @author leegoo
 * @date 2020年03月20日
 */
public enum BankCodeEnum {
    /**
     * @Description:支付宝
     * @param: 
     * @return: 
     * @auther: liming
     * @date: 3/20/2020 9:22 PM
     */
    ZHIFUBAO("ZHIFUBAO","支付宝"),
    /**
     * @Description:
     * @param: 微信
     * @return:
     * @auther: liming
     * @date: 3/20/2020 9:22 PM
     */
    WEIXIN("WEIXIN","微信"),
    ;

    BankCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code ;
    private String desc ;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
