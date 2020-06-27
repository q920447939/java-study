/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年04月19日
 */
package cn.withme.arithmetic.node;

import org.junit.Before;
import org.junit.Test;

/**
 *@description:获取链表长度
 *@author: liming
 *@date: 4/19/2020 6:05 PM
 */

public class NodeTest {

    private  Node<Object> node = new Node<>(1);

    @Before
    public void init () {
        node.setNext(new Node<>(2,new Node<>(3,null)));
        printNode();
    }

    @Test
    public void calNodeLength() {
        int length = this.getNodeLength(node);
        System.out.println(length);
    }

    /**
     * @description: 获取链表的长度,不包含头结点
     * @param node 链表
     * @return: int
     * @author: liming
     * @date: 4/19/2020 6:18 PM
     */
    private int getNodeLength(Node<Object> node) {
        int len = 0;
        if (null == node || null == node.getNext()) return len;

        Node<Object> tmp = node;
        while (null != tmp.getNext()){
            tmp = tmp.getNext();
            len++;
        }
        return len;
    }

    @Test
    public  void calReIndexByNode(){
        int reIndex = 1;
        Node node1 = this.getReIndexByNode(node,reIndex);
        printThisNode(node1);
    }

    /**
     * @description: 获取链表的倒数第k个节点,倒数第k个不能为头结点
     * @param node
     * @param reIndex
     * @return: cn.withme.arithmetic.node.Node<java.lang.Object>
     * @author: liming
     * @date: 4/19/2020 6:27 PM
     */
    private Node<Object> getReIndexByNode(Node<Object> node, int reIndex) {
        if (null == node || reIndex <= 0) return null;

        int length = getNodeLength(node);
        if (length < reIndex) return null;

        Node<Object> tmp = node.getNext();
        for (int i = 0 , index = length - reIndex; i < index; i++) {
            tmp = tmp.getNext();
        }
        return tmp;
    }


    @Test
    public void reverseNode1() {
        Node<Object> reverseNode1 =  this.calReverseNode1(node);
        printNode(reverseNode1);
    }

    private Node<Object> calReverseNode1(Node<Object> node) {
        if (null == node) return null;
        Node<Object> reverseNode,finalReverseNode = new Node<>(node.getT());
        Node<Object> tmp = node.getNext() ;
        while (null != tmp){
            reverseNode= new Node<>();
            reverseNode.setT(tmp.getT());
            tmp = tmp.getNext();
        }
        return finalReverseNode;
    }


    public  void  printNode () {
        System.err.println(node);
    }

    public  void  printNode (Node node) {
        System.err.println(node);
    }

    public  void printThisNode(Node node){
        System.err.println("node:"+node.getT());
    }
}
