/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月12日
 */
package cn.withme.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: ThreadPoolExceptionMes
 * 线程池打印异常.
 *
 * @author leegoo
 * @Description:
 * @date 2019年07月12日
 */
public class ThreadPoolExceptionMsg {
    static MyThreadPool myThreadPool = new MyThreadPool(
            10, 20, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    public static class MyThreadPool extends ThreadPoolExecutor {
        public MyThreadPool(
                int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

//
//        @Override
//        public void execute(Runnable command) {
//            super.execute(wrap(command, myExcetion()));
//        }

        @Override
        public Future<?> submit(Runnable task) {
            return super.submit(wrap(task, myExcetion()));
        }

        private Exception myExcetion() {
            return new RuntimeException(Thread.currentThread().getName()+"发生异常啦....");
        }

        private Runnable wrap(final Runnable runnable, Exception myExcetion) {
            return () -> {
                try {
                    Thread thread1 = (Thread)runnable;
                    thread1.start();
                } catch (Exception e) {
                    myExcetion.printStackTrace();
                    throw e;
                }
            };
        }
    }

    public static class MyThread extends Thread {
        int numberA, numberB;

        public MyThread(int numberA, int numberB) {
            this.numberA = numberA;
            this.numberB = numberB;
        }

        @Override
        public void run() {
            System.out.println("计算numberA/numberB的结果为:" + numberA / numberB);
        }
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        for (int i = 0; i < 5; i++) {
            myThreadPool.submit(new MyThread(100, i));
        }
        myThreadPool.shutdown();
    }
}
