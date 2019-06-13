/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月12日
 */
package cn.withme.arithmetic;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName: Boy
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月12日
 */

@Setter
@Getter
public class Boy {
    private Boy pre;
    private int no;
    private Boy next;

    public Boy(int i) {
        this.no = i;
    }

    @Override
    public String toString() {
        return "Boy{" +
                "pre.no=" + (null == pre ? null : pre.getNo()) +
                ", no=" + no +
                ", next.no=" + (null == next ? null : next.getNo()) +
                '}';
    }
}
