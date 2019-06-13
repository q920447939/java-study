/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月11日
 */
package cn.withme.arithmetic;




/**
 * ClassName: CyclicNodeTest
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月11日
 */

public class CyclicNodeTest {
    static CyclicNode n5 = new CyclicNode();
    static CyclicNode n4 = new CyclicNode();
    static CyclicNode n3 = new CyclicNode();
    static CyclicNode n2 = new CyclicNode();
    static CyclicNode n1 = new CyclicNode();

    static {
        n1.setVal(1);
        n2.setVal(2);
        n3.setVal(3);
        n4.setVal(4);
        n5.setVal(5);
        n1.setNext(n2);
        n2.setNext(n3);
        n3.setNext(n4);
        n4.setNext(n5);
        n5.setNext(n1);
    }

    public static void main(String[] args) {
        todoing(n1,2,2);
    }


    /**
     * @Description:
     * @param: [head 头结点, k 第一次的需要跳转的节点, m 每次步进的次数]
     * @return: CyclicNode
     * @auther: liming
     * @date: 2019/6/11 22:31
     */
    public static CyclicNode todoing (CyclicNode head, int k , int m) {
        if (null == head || head.getNext() ==null) return head;
        CyclicNode result = new CyclicNode();
        CyclicNode temp = head; CyclicNode add = new CyclicNode();
        for (int count = 0; ; count++) {
            if (k != -1 && k==count) {
                add.setNext(temp);
                CyclicNode next = temp.getNext();
                temp.setNext(temp.getNext().getNext());
                temp = next;add =add.getNext();
                if (null == temp.getNext()) {
                    break;
                }
                k = -1;
            }else if(k==-1 && count>m && count%m==0){
                add.setNext(temp);
                CyclicNode next = temp.getNext();
                temp.setNext(temp.getNext().getNext());
                temp = next;add =add.getNext();
                if (null == temp.getNext()) {
                    break;
                }
            }else if(k==-1 && count<m && m%count==0){
                add.setNext(temp);
                CyclicNode next = temp.getNext();
                temp.setNext(temp.getNext().getNext());
                temp = next;add =add.getNext();
                if (null == temp.getNext()) {
                    break;
                }
            }else{

            }
        }
        return result;

    }

    public void trans(CyclicNode temp, CyclicNode add){

    }


}
