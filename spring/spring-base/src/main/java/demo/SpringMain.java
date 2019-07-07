package demo; /**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月14日
 */

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ClassName: demo.SpringMain
 * @Description:
 * @author leegoo
 * @date 2019年05月14日
 */
public class SpringMain {
    public static void main(String[] args) {
        BeanFactory context = new ClassPathXmlApplicationContext("application.xml");
        MessageImpl bean = context.getBean("messageImpl", MessageImpl.class);
        bean.send("1");
        ((ClassPathXmlApplicationContext) context).close();
    }
}
