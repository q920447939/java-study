/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月21日
 */
package arithmetic;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: ReLink
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月21日
 */
public class ReLink {
    public static class Node {
        private int val;
        private Node next;

        public Node(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "val=" + val +
                    ", next=" + next +
                    '}';
        }
    }

    public static void main(String[] args) {
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        n1.next = n2;
        n2.next = n3;
        System.out.println(reList2(n1));
    }

    private static Node reList(Node head) {
        Node prev = null; //前指针节点
        //每次循环，都将当前节点指向它前面的节点，然后当前节点和前节点后移
        while (head != null) {
            // 2 3
            Node nextTemp = head.next; //临时节点，暂存当前节点的下一节点，用于后移
            // 1 next null
            head.next = prev; //将当前节点指向它前面的节点
            //
            prev = head; //前指针后移
            head = nextTemp; //当前指针后移
        }
        return prev;
    }


    private static Node reList2(Node head) {
        // 1 next =2   next = 3
        Node res = null;
        while (null != head) {
            //  2  3    ,   3  ,  null
            Node temp = head.next;
            // null      2,next = 1  ,   next =2 ,next =1
            head.next = res;
            // res =    1  next = null  ,   2,next = 1    ,   ,va =3 ,next =2 ,next =1
            res = head;

            // head  = 2 next  =3   ,3
            head = temp;
        }
        return res;
    }


}
