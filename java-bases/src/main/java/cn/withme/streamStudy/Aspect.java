/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年07月23日
 */
package cn.withme.streamStudy;

import org.junit.Test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: Aspect
 * @Description:
 * @author leegoo
 * @date 2021年07月23日
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Aspect {
    Class clz();
}
