/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月21日
 */
package thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ClassName: writerAndRead
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月21日
 */
public class WriterAndRead {
    public static volatile Integer val = 0;
    static ReentrantReadWriteLock writeLock = new ReentrantReadWriteLock();


    public static void main(String[] args) {
        BlockingQueue<Runnable> blockQue = new ArrayBlockingQueue<>(1);
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2, 3, 1, TimeUnit.MINUTES, blockQue);
        poolExecutor.execute(new WriterAndRead().execute());
        poolExecutor.execute(new WriterAndRead().execute());
        poolExecutor.execute(new WriterAndRead().execute());
        poolExecutor.shutdown();
    }

    public Runnable execute() {
        return () -> write();
    }

    public void read() {
        writeLock.readLock().lock();
        System.out.println("this value is " + val);
        writeLock.readLock().unlock();
    }

    public void write() {
        writeLock.writeLock().lock();
        try {
            System.out.println(" write method,this value is " + val++);
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            writeLock.writeLock().unlock();
        }
    }

}
