/**
 * @Project:
 * @Author: leegoo
 */
package cn.withme.myvolatile;

/**
 * ClassName: VolatileTest01
 *
 * @author leegoo
 * @Description:
 */
public class VolatileTest01 {

    public  volatile static  long number = 0;
    private  static  boolean ready =false ;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            //当 ready = true 的时候满足条件 跳出循环 ,执行下面的打印语句
            //但是下方代码由于加了 Thread.sleep(1000);
            //导致 thread 先执行,导致读取到的read的值是 false
            while (!ready){
                //奇怪的是 如果加一条打印一句,程序是可以停止的
                //这可能就是虚拟机"优化代码"的结果吧
                //System.out.println("ready:"+ready);
            }

            System.out.println(number);
        });
        thread.start();
        Thread.sleep(1000);

        number =  1000;

        ready = true;

    }
}
