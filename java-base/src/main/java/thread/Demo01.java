/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月21日
 */
package thread;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: Demo01
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月21日
 */
public class Demo01 {
    int a;
    int b = 2;

    public Demo01() {
        a++;
        b++;
        System.out.println(a);
        System.out.println(b);
    }

    public static void main(String[] args) {

        BlockingQueue<Runnable> blockQue = new ArrayBlockingQueue<>(1);
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, 3, 1, TimeUnit.MINUTES, blockQue);
        //超过最大线程承受(maximumPoolSize) 会抛异常
        for (int i = 0; i < 4; i++) {
            createThread(poolExecutor);
        }
        poolExecutor.shutdown();
    }

    public static void createThread(ThreadPoolExecutor poolExecutor) {
        Runnable r1 = () -> {
            System.out.println(Thread.currentThread().getName() + "开始睡眠了...");
            try {
                Thread.sleep(3_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "睡眠结束了...");

        };
        poolExecutor.execute(r1);
    }

}
