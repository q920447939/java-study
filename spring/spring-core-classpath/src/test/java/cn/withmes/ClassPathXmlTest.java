package cn.withmes;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ClassName: ClassPathXmlTest
 *
 * @author liming
 * @Description:
 * @date 2022年09月30日
 */
//@RunWith(SpringJUnit4ClassRunner.class)
public class ClassPathXmlTest {

    @Test
    public void classPathStart () {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-base-application.xml");
        Message message = context.getBean(Message.class);
        System.out.println(message.getName());
    }
}
