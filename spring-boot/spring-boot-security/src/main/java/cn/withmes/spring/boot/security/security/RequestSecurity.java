/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月01日
 */
package cn.withmes.spring.boot.security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * ClassName: RequestSecurity
 * @Description:
 * @author leegoo
 * @date 2019年07月01日
 */
@EnableWebMvcSecurity
public class RequestSecurity extends WebSecurityConfigurerAdapter {
    @Autowired
    private  MyPasswordEncoder myPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.err.println("configure方法");
        auth.inMemoryAuthentication()
                .passwordEncoder(myPasswordEncoder)
                .withUser("user").password("123") .roles("USER").and()
                .withUser("admin").password("123") .roles("ADMIN")
               ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/resources/**", "/signup", "/about").permitAll()  //静态资源不拦截
                .antMatchers("/admin/**").hasRole("ADMIN")  //访问请求/admin/*将会被拦截
                .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
               // .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA') and hasIpAddress('127.0.0.1')") 限制IP
                .anyRequest().authenticated()
                .and().rememberMe().tokenValiditySeconds(20000).key("abcd") //记住用户
                .and().logout().logoutSuccessUrl("/logout") //登出
                //.and().requiresChannel().antMatchers("/secure").requiresSecure() //需要HTTPS
                .and().formLogin()
        ;
    }

    @Component
    class MyPasswordEncoder implements PasswordEncoder{
        @Override
        public String encode(CharSequence charSequence) {
            return charSequence.toString();
        }

        @Override
        public boolean matches(CharSequence charSequence, String s) {
            return s.equals(charSequence.toString());
        }
    }
}
