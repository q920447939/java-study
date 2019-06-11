/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月28日
 */
package thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * ClassName: ThreadReject
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月28日
 */
public class ThreadReject {

    //丢弃任务并抛出RejectedExecutionException异常。
    public static class AbortPolicy implements RejectedExecutionHandler {
        // 抛出异常
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + e.toString());
        }
    }


    public static void main(String[] args) {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("demo-%d").build();
        ThreadPoolExecutor executor = null;
        try {
            executor = new ThreadPoolExecutor(2, 3, 60, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(1), factory,
                    //new ThreadPoolExecutor.CallerRunsPolicy()  由调度线程处理任务,此处的调度线程是main 线程,所以会由main执行该任务
                    new ThreadPoolExecutor.CallerRunsPolicy());
            for (int i = 0; i < 6; i++) {
                Future<?> submit = executor.submit(() -> {
                    try {
                        System.out.println(Thread.currentThread().getName() + "开始睡眠了...");
                        Thread.sleep(3_000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                });
                System.out.println("任务是否完成:" + submit.isDone());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
