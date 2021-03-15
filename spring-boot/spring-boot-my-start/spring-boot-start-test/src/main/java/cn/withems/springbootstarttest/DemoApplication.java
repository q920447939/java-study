package cn.withems.springbootstarttest;

import cn.withmes.springbootmystartconfig.DemoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);
        DemoConfig config = applicationContext.getBean(DemoConfig.class);
        System.out.println(config);
    }

}
