/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月06日
 */
package cn.withmes.spring.cloud.eureka.client01.config;

import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: FeignConfigure
 * @Description:
 * @author leegoo
 * @date 2019年07月06日
 */
@Configuration
public class FeignConfigure {
    public static int connectTimeOutMillis = 3_000;//超时时间
    public static int readTimeOutMillis = 3_000;
    @Bean
    public Request.Options options() {
        return new Request.Options(connectTimeOutMillis, readTimeOutMillis);
    }

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default();
    }


}
