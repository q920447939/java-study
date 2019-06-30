package cn.withmes.spring.springboot.action;

import cn.withmes.spring.springboot.action.codition.MagicCondition;
import cn.withmes.spring.springboot.action.injuryproperty.Cafe;
import cn.withmes.spring.springboot.action.pojo.ExPlorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@SpringBootApplication(scanBasePackages = "cn.withmes.spring.springboot.action")
@Conditional(value = MagicCondition.class) //条件创造bean
@PropertySource("classpath:cafe.properties") //读取配置文件的值 进行注入
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


    @Autowired
    Environment environment;


    @Bean()
    public Cafe instance() {
        String color = environment.getProperty("color");
        String weight = environment.getProperty("weight");

        return new Cafe(
                color,
                Integer.valueOf(weight));
    }


}
