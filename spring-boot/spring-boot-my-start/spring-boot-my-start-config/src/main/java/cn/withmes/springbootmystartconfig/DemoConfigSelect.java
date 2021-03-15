/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年03月15日
 */
package cn.withmes.springbootmystartconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * ClassName: DemoConfigSelect
 *
 * @author leegoo
 * @Description:
 * @date 2021年03月15日
 */
@Import(DemoConfig.class)
public class DemoConfigSelect {

    @Bean
    @ConditionalOnMissingBean(value = DemoConfig.class)
    public DemoConfig demoConfig() {
        return new DemoConfig();
    }
}
