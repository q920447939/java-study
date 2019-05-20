package spring.start.demo; /**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月14日
 */

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * ClassName: spring.start.demo.MessageImpl
 * @Description:
 * @author leegoo
 * @date 2019年05月14日
 */
public class MessageImpl implements  Message {

    @Override
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
