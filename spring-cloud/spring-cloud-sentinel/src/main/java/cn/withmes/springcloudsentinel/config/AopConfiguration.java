/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月23日
 */
package cn.withmes.springcloudsentinel.config;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: AopConfiguration
 * @Description:
 * @author leegoo
 * @date 2019年05月23日
 */
@Configuration
public class AopConfiguration {

    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

}
