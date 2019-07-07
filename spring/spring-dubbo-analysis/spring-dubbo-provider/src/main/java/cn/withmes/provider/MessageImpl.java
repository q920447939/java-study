package cn.withmes.provider; /**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月14日
 */

/**
 * ClassName: demo.MessageImpl
 * @Description:
 * @author leegoo
 * @date 2019年05月14日
 */
public class MessageImpl  {

    public Object send(Object o) {
        System.out.println("send mes");
        return null;
    }


    private void init() {
        System.out.println("init....");
    }

    private void destory() {
        System.out.println("destory");
    }
}
