/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月11日
 */
package arithmetic;



/**
 * ClassName: CyclicNode
 * @Description:
 * @author leegoo
 * @date 2019年06月11日
 */

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

    public CyclicNode getPre() {
        return pre;
    }

    public void setPre(CyclicNode pre) {
        this.pre = pre;
    }

    public CyclicNode getNext() {
        return next;
    }

    public void setNext(CyclicNode next) {
        this.next = next;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
}
