/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月26日
 */
package cn.withme.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ClassName: WriteAndRead
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月26日
 */
public class WriteAndReadTest {
    static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    static Lock lock = readWriteLock.writeLock();
    static Lock readLock = readWriteLock.readLock();


    public static void read() {
        readLock.lock();
        try {
            System.out.println("read.....");
        } finally {
            readLock.unlock();
        }
    }


    public static void write() {
        lock.lock();
        try {
            System.out.println("sleep.....");
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            service.submit(() -> {
                write();
            });
            service.submit(() -> {
                read();
            });
        }

        service.shutdown();
    }
}
