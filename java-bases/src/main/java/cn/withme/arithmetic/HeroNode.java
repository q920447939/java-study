/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月11日
 */
package cn.withme.arithmetic;

/**
 * ClassName: HeroNode
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月11日
 */
public class HeroNode implements Cloneable {

    private int id;
    private int val;
    private HeroNode next;

    public HeroNode() {
    }

    public HeroNode(int id) {

        this.id = id;
    }

    public HeroNode(int id, int val) {
        this.id = id;
        this.val = val;
    }

    public HeroNode(int id, int val, HeroNode next) {
        this.id = id;
        this.val = val;
        this.next = next;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public HeroNode getNext() {
        return next;
    }

    public void setNext(HeroNode next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "HeroNode{" +
                "id=" + id +
                ", val=" + val +
                ", next=" + next +
                '}';
    }

    public Object clone() throws CloneNotSupportedException {
        HeroNode newStudent = (HeroNode) super.clone();
        if (null != next) {
            newStudent.next = (HeroNode) next.clone();

        }
        return newStudent;
    }


}
