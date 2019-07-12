/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月12日
 */
package cn.withme.thread;

/**
 * ClassName: BadSynOnInteger
 * @Description:
 * @author leegoo
 * @date 2019年07月12日
 */
public class BadSynOnInteger implements  Runnable{
    static BadSynOnInteger badSynOnInteger = new BadSynOnInteger();
    static  Integer i  = 0 ;

    @Override
    public void run() {
        for (int l = 0; l < 1000000; l++) {
            synchronized (i) {
                i++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BadSynOnInteger badSynOnInteger = new BadSynOnInteger();
        Thread t1 = new Thread(badSynOnInteger);
        Thread t2 = new Thread(badSynOnInteger);
        t1.start(); t2.start();
        t1.join(); t2.join();
        System.out.println(i);
    }
}
