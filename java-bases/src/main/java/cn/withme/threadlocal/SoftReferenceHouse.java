/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月27日
 */
package cn.withme.threadlocal;

/**
 * ClassName: SoftReferenceHouse
 * @Description:
 * @author leegoo
 * @date 2019年05月27日
 */
public class SoftReferenceHouse {
    public static void main(String[] args) {

    }
}

class  Hourse {
    public Door[] doors = new Door[2000];

    class Door {}
}
