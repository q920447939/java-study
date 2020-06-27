/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月26日
 */
package cn.withme.pattern.strategy5.test1.domain;

/**
 * ClassName: 圆通快递公司
 *
 * @author leegoo
 * @Description:
 * @date 2020年06月26日
 */

public class YuanTongExpressCompany extends ExPressCompany {
    public YuanTongExpressCompany(double price, int speed, int piece) {
        this.price = price;
        this.speed = speed;
        this.piece = piece;
    }
}
