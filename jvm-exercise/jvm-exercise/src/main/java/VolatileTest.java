/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年02月22日
 */

/**
 * ClassName: VolatileTest
 * @Description:
 * @author leegoo
 * @date 2023年02月22日
 */
public class VolatileTest {
    public static volatile int race = 0;
    public static void increase() {
        race++;
    }
    private static final int THREADS_COUNT = 100;
    public static void main(String[] args) throws InterruptedException {
        System.out.println("start");
        Thread[] threads = new Thread[THREADS_COUNT];
        for (int i = 0; i < THREADS_COUNT; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 100000; i++) {
                        increase();
                    }
                }
            },"t"+i);
            threads[i].start();
            threads[i].join();
        }
// 等待所有累加线程都结束
       //while (Thread.activeCount() > 1)
       //    Thread.yield();
        System.out.println(race);
    }
}
