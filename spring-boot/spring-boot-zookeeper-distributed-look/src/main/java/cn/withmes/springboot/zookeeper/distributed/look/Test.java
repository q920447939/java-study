/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月29日
 */
package cn.withmes.springboot.zookeeper.distributed.look;

/**
 * ClassName: Test
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月29日
 */
public class Test {
    public static void doSomething() {
        System.out.println("线程【" + Thread.currentThread().getName() + "】正在运行...");
    }

    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            public void run() {
                ZooKeeperLock lock = null;
                lock = new ZooKeeperLock("127.0.0.1:2181", "/locks", "test1");
                if (lock.lock()) {
                    doSomething();
                    try {
                        Thread.sleep(1000);
                        lock.unlock();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(runnable);
            t.start();
        }
    }
}
