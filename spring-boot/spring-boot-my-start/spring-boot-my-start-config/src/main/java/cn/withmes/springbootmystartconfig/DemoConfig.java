/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年03月15日
 */
package cn.withmes.springbootmystartconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ClassName: DemoConfig
 *
 * @author leegoo
 * @Description:
 * @date 2021年03月15日
 */
@ConfigurationProperties(prefix = "demo")
@Setter
@Getter
public class DemoConfig {
    private String name;

    @Override
    public String toString() {
        return "DemoConfig{" +
                "name='" + name + '\'' +
                '}';
    }
}
