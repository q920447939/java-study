/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月26日
 */
package cn.withme.pattern.strategy5.test2.domain;

import cn.withme.pattern.strategy5.test2.MyComparator;
import lombok.Getter;
import lombok.Setter;

/**
 * ClassName: ExPressCompany
 * @Description:
 * @author leegoo
 * @date 2020年06月26日
 */
@Setter
@Getter
public abstract class BaseExPressCompany implements MyComparator<BaseExPressCompany> {
      double price; //价格
     int speed; //快递发送速度
     int piece; //件数

    @Override
    public String toString() {
        return "ExPressCompany{" +
                "price=" + price +
                ", speed=" + speed +
                ", piece=" + piece +
                '}';
    }
}
