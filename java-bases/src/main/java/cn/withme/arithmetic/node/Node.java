/**
 * @Project: 链表
 * @Author: leegoo
 * @Date: 2020年04月19日
 */
package cn.withme.arithmetic.node;

import lombok.Data;

/**
 * ClassName: Node
 * @Description:
 * @author leegoo
 * @date 2020年04月19日
 */
@Data
public class Node<T> {
    private T t;
    private  Node<T> next ;

    public Node() {
    }

    public Node(T t) {
        this.t = t;
    }

    public Node(T t, Node<T> next) {
        this.t = t;
        this.next = next;
    }
}
