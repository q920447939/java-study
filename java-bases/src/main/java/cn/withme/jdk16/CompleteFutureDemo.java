/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年09月24日
 */
package cn.withme.jdk16;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName: CompleteFutureDemo
 * @Description:
 * @author leegoo
 * @date 2021年09月24日
 */
public class CompleteFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> completableFutureOne = new CompletableFuture<>();

        ExecutorService cachePool = Executors.newCachedThreadPool();
        cachePool.execute(() -> {
            try {
                Thread.sleep(3000);
                completableFutureOne.complete("异步任务执行结果");
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        // WhenComplete 方法返回的 CompletableFuture 仍然是原来的 CompletableFuture 计算结果.
        CompletableFuture<String> completableFutureTwo = completableFutureOne.whenComplete((s, throwable) -> {
            System.out.println("当异步任务执行完毕时打印异步任务的执行结果: " + s);
        });


        // ThenApply 方法返回的是一个新的 completeFuture.
        CompletableFuture<Integer> completableFutureThree = completableFutureTwo.thenApply(s -> {
            System.out.println("当异步任务执行结束时, 根据上一次的异步任务结果, 继续开始一个新的异步任务!");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return s.length();
        });

        System.out.println("阻塞方式获取执行结果:" + completableFutureThree.get());

        cachePool.shutdown();
        System.out.println("main运行结束。。。");
    }

}
