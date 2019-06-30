/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月28日
 */
package cn.withmes.spring.cloud.bus.event;

import org.springframework.stereotype.Component;

/**
 * ClassName: Monkey
 * @Description:
 * @author leegoo
 * @date 2019年06月28日
 */
@Component
public class Monkey {

    private Integer leg ;

    public int getLeg() {
        return leg;
    }

    public void setLeg(Integer leg) {
        this.leg = leg;
    }

    @Override
    public String toString() {
        return "Monkey{" +
                "leg=" + leg +
                '}';
    }
}
