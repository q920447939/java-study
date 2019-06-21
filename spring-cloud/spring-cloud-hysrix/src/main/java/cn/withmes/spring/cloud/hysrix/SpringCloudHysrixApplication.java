package cn.withmes.spring.cloud.hysrix;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableHystrix
public class SpringCloudHysrixApplication {

    public static void main(String[] args) {
        Map<String, Object> params = new HashMap<>();
        params.put("server.port", 65018);
        params.put("spring.application.name", SpringCloudHysrixApplication.class.getSimpleName());
        params.put("management.server.port", 9002);
        params.put("management.endpoints.web.exposure.include", "*");
        new SpringApplicationBuilder(SpringCloudHysrixApplication.class)
                .properties(params)
                .run(args);
    }

}
