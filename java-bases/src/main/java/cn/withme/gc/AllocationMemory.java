/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月14日
 */
package cn.withme.gc;

/**
 * ClassName: AllocationMemory
 * @Description:
 * @author leegoo
 * @date 2019年06月14日
 */
public class AllocationMemory {

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(10000);
        byte [] bytes = null;
        for (int i = 0; i < 20; i++) {
            bytes = new byte[1*1024*1024];
        }
        Thread.sleep(150000);
    }
}
