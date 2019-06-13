/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月28日
 */
package cn.withme.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * ClassName: CountDownLatchTest
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月28日
 */
public class CountDownLatchTest {

    public static class File implements Runnable {
        private CountDownLatch count;
        private int sleepTime;
        private String dirName;

        public File(CountDownLatch count, int sleepTime, String dirName) {
            this.count = count;
            this.sleepTime = sleepTime;
            this.dirName = dirName;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + ":即将睡眠" + sleepTime + "秒");
                Thread.sleep(sleepTime);
                count.countDown();
                System.out.println(dirName + ":统计完毕,共耗时:" + sleepTime + "秒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static Map<Integer, String> dirMap = new HashMap<>();

    static {
        dirMap.put(0, "C盘");
        dirMap.put(1, "D盘");
        dirMap.put(2, "E盘");
    }

    public static void main(String[] args) throws InterruptedException {

        //如果初始化的值大于线程数 也就是初始化假设为5,但是线程数为3,那么还有两个CountDownLatch没有被使用,导致CountDownLatch一直大于0,那么会被堵塞
        CountDownLatch count = new CountDownLatch(2);

        BlockingQueue<Runnable> que = new ArrayBlockingQueue<>(10);
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("线程-%d").build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 5, 60, TimeUnit.SECONDS, que, factory);

        for (int i = 0; i < 3; i++) {
            File file = new File(count, 2000, dirMap.get(i));
            new Thread(file).start();
        }
        count.await();

        //等于0 说明任务已经统计完成
        System.out.println("最后结果为:" + count.getCount());
        executor.shutdown();
    }
}
