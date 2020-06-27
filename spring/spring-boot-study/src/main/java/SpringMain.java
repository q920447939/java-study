/**
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
    }
}
