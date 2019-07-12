/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月11日
 */
package cn.withme.thread;

/**
 * ClassName: Join
 *
 * @author leegoo
 * @Description:
 * @date 2019年07月11日
 */
public class Join {

    public  volatile  static  int i = 0;

    public  static  class AddThread extends  Thread{

        @Override
        public void run() {
//            for (i= 0; i < 1000; i++) { }
            try {
                Thread.sleep(1000);
                System.out.println("当前线程:"+Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AddThread t1 = new AddThread();
        t1.start();


        AddThread t2 = new AddThread();
        t2.start();

        t2.join();
        //System.out.println(i);

    }
}
