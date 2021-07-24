/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年03月22日
 */
package cn.withems.springbootstarttest;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ClassName: ReenTranLockTest
 * @Description:
 * @author leegoo
 * @date 2021年03月22日
 */
public class ReenTranLockTest {

    public static void main(String[] args) {
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        Lock lock = readWriteLock.readLock();
        lock.lock();

    }
}
