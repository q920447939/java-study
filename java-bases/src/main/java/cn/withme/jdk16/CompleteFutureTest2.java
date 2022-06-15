/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年09月24日
 */
package cn.withme.jdk16;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * ClassName: CompleteFutureTest2
 *
 * @author leegoo
 * @Description:
 * @date 2021年09月24日
 */
public class CompleteFutureTest2 {

    private static String f1V = null;
    private static String f2V = null;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        method();
    }

    private static void method() throws ExecutionException, InterruptedException {
        long t1 = System.currentTimeMillis();
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            f1V = "f1";
            return "f1";
        });

        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            f2V = "f2";
            return "f2";
        });

        CompletableFuture<Void> all = CompletableFuture.allOf(f1, f2);

        //阻塞，直到所有任务结束。
        System.out.println(System.currentTimeMillis() + ":阻塞");
        all.join();
        if (all.isDone()){
            System.out.println(System.currentTimeMillis() + ":阻塞结束");

            //一个需要耗时2秒，一个需要耗时3秒，只有当最长的耗时3秒的完成后，才会结束。

            long t2 = System.currentTimeMillis();
            System.out.println("任务均已完成。耗时=" + (t2 - t1) + ",,,,, f1v,f2v= " + f1V  + "========="+ f2V) ;
        }
    }
}
