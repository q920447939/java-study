/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月05日
 */
package cn.withmes.spring.cloud.eureka.server02.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * ClassName: WebSecurityConfig
 * @Description:
 * @author leegoo
 * @date 2019年07月05日
 */
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override

    protected void configure(HttpSecurity http) throws Exception {

        super.configure(http);

        http.csrf().disable();

    }}
