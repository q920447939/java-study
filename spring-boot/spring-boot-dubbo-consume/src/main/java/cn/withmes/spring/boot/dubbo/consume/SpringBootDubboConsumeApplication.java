package cn.withmes.spring.boot.dubbo.consume;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration
public class SpringBootDubboConsumeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDubboConsumeApplication.class, args);
    }

}
