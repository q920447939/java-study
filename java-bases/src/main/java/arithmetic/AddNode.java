package arithmetic;

import domain.HeroNode;

public class AddNode {


    public static void main(String[] args) {
        HeroNode n1 = new HeroNode(1);
        HeroNode n2 = new HeroNode(2);
        n2.setIud(2);
        HeroNode n4 = new HeroNode(4);
        n1.setNext(n2);
        n2.setNext(n4);
        add(n1, new HeroNode(4, null));
        System.out.println(n1);
        HeroNode updateData = new HeroNode();
        updateData.setName("王大锤");
        updateData.setIud(2);
        update(n1, updateData);
        System.out.println(n1);

        HeroNode reKNode = findReKNode(n1, 1);
        System.out.println(reKNode);
    }


    /**
     * 链表的添加方法
     */
    public static HeroNode add(HeroNode head, HeroNode data) {
        HeroNode temp = head;
        for (; ; ) {
            HeroNode next = temp.getNext();
            if (null == next) return temp;
            if (next.getVal() > data.getVal()) {
                temp.setNext(data);
                data.setNext(next);
                break;
            } else {
                temp = next;
            }
        }
        return temp;
    }

    /**
     * 修改链表
     *
     * @param head
     * @param data
     * @return
     */
    public static HeroNode update(HeroNode head, HeroNode data) {
        HeroNode temp = head;
        for (; ; ) {
            HeroNode next = temp.getNext();
            if (null == next) return temp;
            if (next.getIud() == data.getIud()) {
                next.setName(data.getName());
                return temp;
            }
            temp = next;
        }

    }


    public static HeroNode findReKNode(HeroNode head, int k) {
        HeroNode cur = head;
        HeroNode res = null;
        while (cur != null) {
            HeroNode next = cur.getNext();
            cur.setNext(res);
            res = cur;
            cur = next;
        }
        if (k == 0) return res;
        for (int i = 0; ; i++) {
            HeroNode next = res.getNext();
            if ( i == k) {
                return res;
            } else if (null == next) {
                return next;
            }
            res = next;
        }
    }
}
