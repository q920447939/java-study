/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月26日
 */
package cn.withme.pattern.strategy5.test2;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName: ExpressDelegate
 * @Description:
 * @author leegoo
 * @date 2020年06月26日
 */
@Setter
@Getter
public class ExpressDelegate {
    int piece ;
    int price ;
    int speed;

    public ExpressDelegate(int piece, int speed) {
        this.piece = piece;
        this.speed = speed;
    }

    public ExpressDelegate() {

    }
}
