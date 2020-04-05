package cn.withmes.springboot.my.aop.scan;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyTransaction {
    //默认是否进行代理
    boolean proxyFlag() default true;
    //需要回滚的方法
    Class<?> callBackClass() ;
    //需要回滚的方法
    String callBackMethod() ;
}
