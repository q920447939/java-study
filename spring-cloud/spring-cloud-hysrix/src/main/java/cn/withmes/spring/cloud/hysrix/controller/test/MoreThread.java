/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月20日
 */
package cn.withmes.spring.cloud.hysrix.controller.test;

import org.apache.commons.lang.math.RandomUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: MoreThread
 * 信号量测试,有点点小BUG
 * @author leegoo
 * @Description:
 * @date 2019年06月20日
 */
public class MoreThread {
    private static volatile ExecutorService executors = Executors.newFixedThreadPool(20);

    private static volatile Semaphore semaphore = new Semaphore(3);

    private static volatile CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args)  {
        for (int i = 0; i < 20; i++) {
            final int value = i;
            executors.execute(() -> {
                try {
                    if (semaphore.tryAcquire(RandomUtils.nextInt(300), TimeUnit.MILLISECONDS)) {
                        int i1 = semaphore.drainPermits();
                        System.out.println(Thread.currentThread().getName() + "获取了剩余的全部" + i1 + "个许可证.");
                        try {
                            Thread.sleep(20);
                            System.out.println("当前线程:" + Thread.currentThread().getName() + "已经执行完毕了...." + "\t i  = " + value);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                    if (value == 19) {
                        countDownLatch.countDown();
                    }
                }
            });
        }

            executors.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(200);
                    countDownLatch.await();
                    System.out.println("已经完成...");
                    executors.shutdown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

    }
}
