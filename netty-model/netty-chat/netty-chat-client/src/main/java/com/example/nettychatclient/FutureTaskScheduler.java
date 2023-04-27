/**
 * Created by 尼恩 at 疯狂创客圈
 */

package com.example.nettychatclient;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class FutureTaskScheduler {
    //方法二是使用自建的线程池时，专用于处理耗时操作
    static ThreadPoolExecutor mixPool = null;

    static {
        mixPool = new ThreadPoolExecutor(
                10,
                10,
                120,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue(10000),
                new ThreadPoolExecutor.AbortPolicy());
    }

    private static FutureTaskScheduler inst = new FutureTaskScheduler();

    private FutureTaskScheduler() {

    }

    /**
     * 添加任务
     *
     * @param executeTask
     */


    public static void add(ExecuteTask executeTask) {
        mixPool.submit(executeTask::exec);
    }



}
