package cn.withmes.spring.springboot.action;

import cn.withmes.spring.springboot.action.pojo.ExPlorer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication(scanBasePackages = "cn.withmes.spring.springboot.action")
public class SpringSpringbootActionApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringSpringbootActionApplication.class, args);
//        ExPlorer exPlorer = applicationContext.getBean(ExPlorer.class);
//        exPlorer.work();
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
//        context.register(ExPlorer.class);
//        context.refresh();
//        ExPlorer exPlorer = context.getBean(ExPlorer.class);
//        exPlorer.work();
    }

}
