/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月10日
 */
package cn.withme.thread;

import lombok.Getter;
import lombok.Setter;

import javax.lang.model.type.UnknownTypeException;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: StopThreadUnsafe
 *
 * @author leegoo
 * @Description: 线程停止方式1
 * @date 2019年07月10日
 */
public class StopThreadUnsafe {


    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                int value = (int) System.currentTimeMillis() / 1000;

                try {
                    System.err.println("value:" + value);
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("线程cathch.");
                    //e.printStackTrace();
                }
            }
        });
        thread.start();
        Thread.sleep(3000);
        thread.interrupt();
    }

}
