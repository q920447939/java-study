package cn.withmes.spring.cloud.bus.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.support.StandardServletEnvironment;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(scanBasePackages = "cn.withmes.spring.cloud.bus.event")
public class SpringCloudBusEventApplication {

    public static void main(String[] args) {
        //ConfigurableApplicationContext context = SpringApplication.run(SpringCloudBusEventApplication.class, args);

//
//        ApplicationEvent event = new ApplicationEventImpl(new String("asdsadasdas"));
//        context.publishEvent(event);
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
//
//        context.register(SpringCloudBusEventApplication.class);
//        ConfigurableEnvironment c = new StandardServletEnvironment();
//        context.setEnvironment(c);
//        context.refresh();
        //new spri

    }

    static  class ApplicationEventImpl extends  ApplicationEvent {

        public ApplicationEventImpl(Object source) {
            super(source);
            System.out.println("source:"+source);

        }

    }




}
