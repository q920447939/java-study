/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月15日
 */
package cn.withme.thread;

import java.util.Random;
import java.util.concurrent.*;

/**
 * ClassName: CallableTest
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月15日
 */
public class CallableTest implements Callable<String> {

    @Override
    public String call() throws Exception {
        Thread.sleep(2_000);
        return "hello"+new Random().nextInt();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> submit = executorService.submit(new CallableTest());
        String result = submit.get();
        System.out.println(result);
        executorService.shutdown();
    }
}
