/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月26日
 */
package cn.withme.pattern.strategy5.test2.domain;

/**
 * ClassName: 顺丰快递公司
 * @Description:
 * @author leegoo
 * @date 2020年06月26日
 */
public class ShunFengExpressCompanyBaseExPressCompany extends BaseExPressCompany {
    public ShunFengExpressCompanyBaseExPressCompany(double price, int speed, int piece) {
        this.price = price;
        this.speed = speed;
        this.piece = piece;
    }

    public ShunFengExpressCompanyBaseExPressCompany() {
    }

    @Override
    public int sort(BaseExPressCompany t1, BaseExPressCompany t2) {
        if (t1.speed < t2.speed) return  1;
        else  if  (t1.speed > t2.speed) return  -1;
        return 0;
    }
}
