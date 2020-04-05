/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月14日
 */
package cn.withmes.springboot.my.aop;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: MyAOP
 * @Description:
 * @author leegoo
 * @date 2020年03月14日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({MyAOPRegister.class})
public @interface MyAOP {
    //扫描包路径
    String[] basePackages() default {};
    //扫描类
    Class<?>[] basePackageClasses() default {};
}
