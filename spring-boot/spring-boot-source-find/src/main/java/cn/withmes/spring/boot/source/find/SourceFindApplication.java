package cn.withmes.spring.boot.source.find;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

//@SpringBootApplication
//@Configurable()    //2
@ComponentScan(value="cn.withmes.spring.boot.source.find")
public class SourceFindApplication {

    public static void main(String[] args) {

        //SpringApplication.run(SourceFindApplication.class, args);


        //spring boot启动的第二种方式, 首先配置一个注解 如"2" ,
        //注解配置启动
//        AnnotationConfigApplicationContext context =
//                new AnnotationConfigApplicationContext();

        //将当前class 注册到bean 容器中
//        context.register(SourceFindApplication.class);

        //上下文切换,我理解的是刷新Bean容器
//        context.refresh();

//        System.out.println(context.getBean(SourceFindApplication.class));


        // 测试自己定义的注解 组合别的注解
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(SourceFindApplication.class);
        context.refresh();

        Monkey bean = context.getBean(Monkey.class);
        System.out.println(bean);


        context.close();

    }

}
