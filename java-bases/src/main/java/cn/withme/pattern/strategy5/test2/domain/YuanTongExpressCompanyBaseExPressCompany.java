/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月26日
 */
package cn.withme.pattern.strategy5.test2.domain;

/**
 * ClassName: 圆通快递公司
 *
 * @author leegoo
 * @Description:
 * @date 2020年06月26日
 */

public class YuanTongExpressCompanyBaseExPressCompany extends BaseExPressCompany {
    public YuanTongExpressCompanyBaseExPressCompany(double price, int speed, int piece) {
        this.price = price;
        this.speed = speed;
        this.piece = piece;
    }

    @Override
    public int sort(BaseExPressCompany t1, BaseExPressCompany t2) {
        //TODO
        return 0;
    }
}
