/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月07日
 */
package cn.withmes.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ClassName: Provider
 * @Description:
 * @author leegoo
 * @date 2019年07月07日
 */
public class Provider {
    public static void main(String[] args) throws Exception {
        System.setProperty("java.net.preferIPv4Stack", "true");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo-provider.xml");
        context.start();
        System.out.println("Provider started.");
        System.in.read(); // press any key to exit
    }
}
