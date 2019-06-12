/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月11日
 */
package domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ClassName: CyclicNode
 * @Description:
 * @author leegoo
 * @date 2019年06月11日
 */
@NoArgsConstructor
@Getter
@Setter
public class CyclicNode {
    private CyclicNode pre;
    private CyclicNode next;
    private int val;

    @Override
    public String toString() {
        return "CyclicNode{" +
                "pre=" + pre +
                ", next=" + next +
                ", val=" + val +
                '}';
    }
}
