/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月23日
 */
package cn.withme.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName: WaitAndNotify
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月23日
 */
public class WaitAndNotify {

    private static Object object = new Object();
    private static volatile AtomicInteger number = new AtomicInteger(1);
    private static volatile AtomicBoolean flg = new AtomicBoolean(false);


    public static void main(String[] args) throws  Exception{

        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("demo-cn.withme.thread-%d").build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024), factory);
        executor.execute(()->{
            try {
                WaitAndNotify.provider();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.execute(()->{
            try {
                WaitAndNotify.consume();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void provider() throws Exception {
        while (true) {
            synchronized (object) {
                if (!flg.get()) {
                    number.getAndIncrement();
                    int numberAndIncrement = number.get();
                    System.out.println(Thread.currentThread().getName()+"生成了数字:" + numberAndIncrement);
                    Thread.sleep(1_000);
                    flg.getAndSet(true);
                    object.wait();
                }
            }

        }
    }


    public static void consume() throws Exception {
        while (true) {
            synchronized (object) {
                if (flg.get()) {
                    int number = WaitAndNotify.number.get();
                    System.out.println(Thread.currentThread().getName() + "消费了数字:" + number);
                    Thread.sleep(1_000);
                    flg.getAndSet(false);
                    object.notify();
                }

            }
        }
    }
}
