package demo; /**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月14日
 */

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ClassName: demo.SpringMain
 * @Description:
 * @author leegoo
 * @date 2019年05月14日
 */
public class SpringMain {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        //MessageImpl bean = context.getBean("messageImpl", MessageImpl.class);
       /* String name = bean.getName();
        System.err.println(name);*/
        //((ClassPathXmlApplicationContext) context).close();
        //Thread.sleep(Integer.MAX_VALUE);

        Teacher teacher = context.getBean("teacher",Teacher.class);
        System.err.println(teacher.getName());
    }
}
