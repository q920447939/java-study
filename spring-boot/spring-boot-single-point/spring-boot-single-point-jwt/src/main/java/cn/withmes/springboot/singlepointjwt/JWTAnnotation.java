/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月22日
 */
package cn.withmes.springboot.singlepointjwt;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: JWTAnnotation
 * @Description: 标记需要拦截的类
 * @author leegoo
 * @date 2019年07月22日
 */

@Documented
@Target({ElementType.METHOD})
@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
public @interface JWTAnnotation  {
}
