/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年07月23日
 */
package cn.withme.streamStudy;

/**
 * ClassName: IOrder
 * @Description:
 * @author leegoo
 * @date 2021年07月23日
 */
public interface IOrder {

    public  void pay () throws Exception ;

    public  void doPay (String str);
}
