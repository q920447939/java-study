package cn.withmes.spring.boot.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@SpringBootApplication(scanBasePackages = "cn.withmes.spring.boot.security")
public class SpringBootSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityApplication.class, args);
    }

    @RestController
    public class  HttpController {

        @GetMapping("/say")
        public String sayName(@RequestParam(name = "name") String name) {
            return "hi ,your name is "+name;
        }


        @GetMapping("/admin/info")
        public String userinfo() {
            return "hi ,welcome to member :";
        }


        @GetMapping("/random/get")
        public int random() {
            return new Random().nextInt();
        }


        @GetMapping("/logout")
        public String logout() {
            return "登出成功..";
        }


    }

}
