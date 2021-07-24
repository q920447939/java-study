/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年07月22日
 */
package cn.withme.streamStudy;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ClassName: OptionTest
 * @Description:
 * @author leegoo
 * @date 2021年07月22日
 */
public class OptionTest {

    static ExecutorService threadPool =
            Executors.newFixedThreadPool(2);

    @Test
    public void test_option1 () {
        System.out.println("123");
    }



    @Test
    public void test_future () throws ExecutionException, InterruptedException {
        Future<String> f1 = threadPool.submit(() -> {
            try {
                Thread.sleep(3_000);
                System.out.println("t1 睡眠完毕!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "t1";
        });
        Future<String> f2 = threadPool.submit(() -> {
            try {
                System.out.println("t2 睡眠完毕!");
                Thread.sleep(2_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "t2";
        });
        System.out.println(f2.get());
        System.out.println(f1.get());
    }

    @Test
    public void testCountDown () {
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        threadPool.execute(()->{
            try {
                Thread.sleep(3_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                countDownLatch.countDown();
            }
        });

        threadPool.execute(()->{
            try {
                Thread.sleep(1_000);
                throw  new RuntimeException("报错啦..");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            System.out.println("countdownlatch err..");
            e.printStackTrace();
        }
        threadPool.shutdown();
        long end = System.currentTimeMillis();

        System.out.println("运行完毕..." + (end-start));
    }

}
