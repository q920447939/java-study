package cn.withmes.springboot.log.desensitization;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.DesensitizedUtil;
import cn.withmes.springboot.log.desensitization.core.LogEncryptClz;
import cn.withmes.springboot.log.desensitization.core.LogEncryptFiled;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class SpringBootLogDesensitizationApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootLogDesensitizationApplication.class);


    public static void main(String[] args) {
        User mock = mockData();
        LOGGER.info("【启动类】打印脱敏对象 mock1={}", mock);
        loadProperties();
        SpringApplication.run(SpringBootLogDesensitizationApplication.class, args);
        LOGGER.info("【启动类】打印脱敏对象 mock2={}", mock);
    }

    private static User mockData() {
        return User.builder().id("1").name("王大锤").password("123456").build();
    }


    @SneakyThrows
    private static void loadProperties() {
        ClassPathResource resource = new ClassPathResource("system.properties");
        Properties properties = new Properties();
        properties.load(resource.getStream());
        properties.forEach((k, v) -> System.setProperty(String.valueOf(k), String.valueOf(v)));
        LOGGER.info("【启动类】获取到环境参数={}", properties);
    }

    @Data
    @Builder
    @LogEncryptClz
    public static class User {
        private String id;
        private String name;
        @LogEncryptFiled(type = DesensitizedUtil.DesensitizedType.PASSWORD)
        private String password;
    }

}
