/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月15日
 */
package cn.withme.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName: ThreadInterupter
 *  测试线程中断,和改变interupter的值
 * @author leegoo
 * @Description:
 * @date 2019年06月15日
 */
public class ThreadInterupter {

    private static  int u = 0;

    public static void main(String[] args) throws InterruptedException {

        Thread my_thread = new Thread(() -> {


            while (!Thread.currentThread().isInterrupted()) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("this is int val is :" + u);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                u++;
            }

            System.out.println("===================");
            System.out.println(u);
        }, "my_thread");

        my_thread.start();
        TimeUnit.SECONDS.sleep(1);
        my_thread.interrupt();
        System.out.println("线程第一次被中断之后interrupt的值:"+my_thread.isInterrupted());
        TimeUnit.SECONDS.sleep(1);
        my_thread.interrupt();
        System.out.println(my_thread.isInterrupted());
        System.out.println("线程第二次被中断之后interrupt的值:"+my_thread.isInterrupted());

    }
}
