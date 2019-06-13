/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月23日
 */
package cn.withme.thread;

/**
 * ClassName: ProduceConsumeVersion2
 * @Description:
 * @author leegoo
 * @date 2019年05月23日
 */
public class ProduceConsumeVersion2 {
    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                ProduceConsumeVersion2.produce();
            }
        }, "produce").start();

        new Thread(() -> {
            while (true) {
                ProduceConsumeVersion2.consume();
            }
        }, "consume").start();
    }

    private static Integer i = 0; //生产的数字
    private static boolean flgProduce = false; //生产状态
    private static Object obj = new Object();


    public static void produce() {
        synchronized (obj) {
            if (flgProduce) {
                try {
                    //生产了数字但是没有被消费,等待消费者消费
                    obj.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //没有生产数字
                //生产一个数字，通知消费者消费
                i++;
                System.out.println("produce i:" + i);
                try {
                    obj.notify();
                    //设置生产状态为已生产
                    flgProduce = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void consume() {
        synchronized (obj) {
            if (flgProduce) {
                //可消费，模拟消费
                System.out.println("consume i:" + i);
                try {
                    //消费完成之后通知生产者继续生成数字
                    obj.notify();
                    //将生产状态改为未生成
                    flgProduce = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //没有可消费的数字
                try {
                    //等待生产者进行生产
                    obj.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
