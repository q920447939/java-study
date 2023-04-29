/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月29日
 */
package com.example.nettychatserver.async;

import lombok.SneakyThrows;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: AsyncCallBack
 *
 * @author leegoo
 * @Description:
 * @date 2023年03月29日
 */
public class AsyncCallBack {

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(1000)
    );


    @SneakyThrows
    public static void run (CallBack callBack){
        Future<?> future =  THREAD_POOL_EXECUTOR.submit(callBack::execute);
        Boolean result = (Boolean) future.get(10, TimeUnit.SECONDS);
        if (result){
            callBack.onSuccess();
        }else {
            callBack.onFail();
        }
    }

}
