package cn.withmes.springboot.singlepointjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cn.withmes.springboot.singlepointjwt")
public class SpringBootSinglePointJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSinglePointJwtApplication.class, args);
    }

}
