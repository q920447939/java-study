/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月17日
 */
package cn.withme.spring.cloud.source.find;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ClassName: SpringCloudApplication
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月17日
 */

@SpringBootApplication(scanBasePackages = "cn.withme.spring.cloud.source.find")
public class SpringCloudApplication {
    private static ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        //配置父的context , 理论上可以无限继承
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //父context 注册id
        context.setId("小明");
        //父context 注册一个bean
        context.registerBean("string hello worild", String.class, "contructor hello wolrd");
        //父context 刷新bean
        context.refresh();
        //context.setParent();
        applicationContext = new SpringApplicationBuilder(SpringCloudApplication.class)
                .parent(context) //设置父 的上下文
                .run(args);

    }

    @RestController
    static class inClaa {

        @Resource(name = "string hello worild")
        public String message;

        @GetMapping("/hello")
        public String hello() {
            //从applicationContext 上下文中拿到 父context的上下文中的bean
            return (String) applicationContext.getBean("string hello worild");
        }


        @GetMapping("/get")
        public Monkey get() {
            return applicationContext.getBean(Monkey.class);
        }


        @GetMapping("/modify")
        public Monkey modify() {
            Monkey bean = applicationContext.getBean(Monkey.class);
            bean.setLeg(3);
            return get();
        }
    }

    @Component
    static class Monkey {
        private int leg = 1;

        public int getLeg() {
            return leg;
        }

        public void setLeg(int leg) {
            this.leg = leg;
        }

        @Override
        public String toString() {
            return "Monkey{" +
                    "leg=" + leg +
                    '}';
        }
    }
}
