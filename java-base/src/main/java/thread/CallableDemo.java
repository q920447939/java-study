/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月21日
 */
package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ClassName: CallableDemo
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月21日
 */
public class CallableDemo {

    static class MyCallable implements Callable {

        @Override
        public Object call() throws Exception {
            try {
                System.out.println("开始睡眠...");
                Thread.sleep(3_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new String("aaabbb:" + Thread.currentThread().getName());
        }

    }

    public static void main(String[] args) throws Exception {
       /* int taskSize = 3;
        //创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(taskSize);
        // 创建多个有返回值的任务
        List<Future> list = new ArrayList<>();
        for (int i = 0; i < taskSize; i++) {
            Callable<MyCallable> c = new MyCallable();
            // 执行任务并获取 Future 对象
            Future f = pool.submit(c);
            list.add(f);
        }
        // 关闭线程池
        pool.shutdown();
        // 获取所有并发任务的运行结果
        for (Future f : list) {
            // 从 Future 对象上获取任务的返回值，并输出到控制台
            System.out.println("res：" + f.get().toString());
        }*/

        ThreadService t = new ThreadService();
        t.run();
        t.start();
    }

    static  class  ThreadService  extends  Thread {
        @Override
        public void run() {
            System.out.println("run ...."+Thread.currentThread().getName());
            super.run();
        }
    }
}
