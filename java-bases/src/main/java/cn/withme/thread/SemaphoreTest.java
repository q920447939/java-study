/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月12日
 */
package cn.withme.thread;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: SemaphoreTest
 *
 * @author leegoo
 * @Description:
 * @date 2019年07月12日
 */
public class SemaphoreTest {
    //允许最多5个线程获取许可证,不公平
    public static final Semaphore semaphore = new Semaphore(5, false);

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread( () ->{
                try {
                    semaphore.acquire();
                    TimeUnit.SECONDS.sleep(3);
                    System.out.println("当前线程"+Thread.currentThread().getId()+"获取到了许可证....");
                    semaphore.release();
                } catch (InterruptedException e) {
                    semaphore.release();
                    e.printStackTrace();
                }
            }).start();
        }
    }


}
