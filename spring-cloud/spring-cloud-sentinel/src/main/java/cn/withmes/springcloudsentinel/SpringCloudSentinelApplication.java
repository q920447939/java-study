package cn.withmes.springcloudsentinel;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SpringCloudSentinelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudSentinelApplication.class, args);
    }

    @Slf4j
    @RestController
    static class TestController {

        @GetMapping("/hello")
        @SentinelResource(value = "spring-cloud-sentinel.hello", blockHandler = "exceptionHandler", fallback = "fallback")
        public String hello() {



            return "didispace.com";
        }

        public String fallback(){
            return "报错啦...";
        }

        // Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
        public String exceptionHandler(long s, BlockException ex) {
            // Do some log here.
            ex.printStackTrace();
            return "Oops, error occurred at " + s;
        }


    }

}
