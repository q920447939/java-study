/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月17日
 */
package cn.withme.queue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: PriorityQueue
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月17日
 */
public class PriorityQueue {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println("正在循环中....");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        TimeUnit.SECONDS.sleep(5);
        thread.start();
    }
}
