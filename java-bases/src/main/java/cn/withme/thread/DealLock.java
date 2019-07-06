/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月01日
 */
package cn.withme.thread;

/**
 * ClassName: DealLock
 *
 * @author leegoo
 * @Description:
 * @date 2019年07月01日
 */
public class DealLock {

    public static void main(String[] args) {
        InnerClass l = new InnerClass();
        new Thread(()->{
            while (true) l.work1();
        }).start();
        new Thread(()->{
            while (true) l.work2();
        }).start();
    }

    public static class InnerClass {
        static  Object objectlock1 = new Object();
        static  Object objectlock2 = new Object();

        public void work1 ()  {
            synchronized (objectlock1) {
                System.out.println("work1线程:"+Thread.currentThread()+"已经锁住了objectlock1,准备获取objectlock2锁");
                synchronized (objectlock2) {
                    System.out.println("work1线程:"+Thread.currentThread()+"已经锁住了objectlock2,当前任务运行完毕");
                }
            }
        }

        public void work2 () {
            synchronized (objectlock2) {
                System.out.println("work2线程:"+Thread.currentThread()+"已经锁住了objectlock2,准备获取objectlock1锁");
                synchronized (objectlock1) {
                    System.out.println("work2线程:"+Thread.currentThread()+"已经锁住了objectlock1,当前任务运行完毕");
                }
            }
        }

    }

}
