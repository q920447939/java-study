/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年07月23日
 */
package cn.withme.streamStudy;

/**
 * ClassName: Order
 * @Description:
 * @author leegoo
 * @date 2021年07月23日
 */
@Aspect(clz=LogRecord.class)
public class Order implements  IOrder{

    @Override
    public  void pay () throws Exception {
        System.out.println("pay method...start..");

        System.out.println("pay method...end..");

    }

    @Override
    public  void doPay (String str) {
        System.out.println("doPay method...start..");
    }

}
