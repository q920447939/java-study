/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月26日
 */
package cn.withme.pattern.strategy5.test1.domain;

/**
 * ClassName: 顺丰快递公司
 * @Description:
 * @author leegoo
 * @date 2020年06月26日
 */
public class ShunFengExpressCompany  extends ExPressCompany{
    public ShunFengExpressCompany(double price, int speed,int piece) {
        this.price = price;
        this.speed = speed;
        this.piece = piece;
    }
}
