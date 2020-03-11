/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月10日
 */

import ioc.Beanfactory;
import ioc.DefaultListableBeanFactory;
import pojo.Student;

/**
 * ClassName: IOCTest
 * @Description:
 * @author leegoo
 * @date 2020年03月10日
 */
public class IOCTest {
    public static void main(String[] args) {
        Beanfactory beanfactory = new DefaultListableBeanFactory("classpath:my.xml");
        Student zs = (Student) beanfactory.getBean("zs");
        System.out.println(zs);
    }
}
