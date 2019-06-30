/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月26日
 */
package cn.withme.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ClassName: ReenTryLockTest
 * @Description:
 * @author leegoo
 * @date 2019年06月26日
 */
public class ReentrantLockTest {

    static ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) {
        reentrantLock.lock();;
    }
}
