/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月14日
 */
package cn.withmes.spring.boot.aop.work;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * ClassName: ProductAOP
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月14日
 */
@Aspect
@Component
public class ProductAOP {

    @Pointcut("execution(public * cn.withmes.spring.boot.aop.work..*Service.*(..))")
    public void add() {

    }

    @Pointcut("execution(public * cn.withmes.spring.boot.aop.work..*Service.*(..))")
    public void delete() {

    }

    @Around("add() || delete()")
    public Object around(ProceedingJoinPoint point) {
        Object result = null;
        Object object = null;
        try {
            Object[] args = point.getArgs();
            object = point.proceed();
            System.out.println("args:"+Arrays.toString(args));
            /*result = point.proceed(point.getArgs());
            String name = point.getSignature().getName();
            Object obj = point.getArgs()[0];
            System.out.println("obj222222222222222:" + obj);
            System.out.println("name:" + name);*/
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
           /* try {
                object = point.proceed();
                System.out.println("finally..." + object);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }*/
        }
        return object;
    }
}
