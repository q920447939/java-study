/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月04日
 */
package cn.withmes;

import java.nio.channels.SelectionKey;

/**
 * ClassName: OpsTest
 * @Description:
 * @author leegoo
 * @date 2023年03月04日
 */
public class OpsTest {
    public static void main(String[] args) {
        System.out.println(SelectionKey.OP_READ);
        System.out.println(SelectionKey.OP_WRITE);
        System.out.println(SelectionKey.OP_CONNECT);
        System.out.println(SelectionKey.OP_ACCEPT);
    }
}
