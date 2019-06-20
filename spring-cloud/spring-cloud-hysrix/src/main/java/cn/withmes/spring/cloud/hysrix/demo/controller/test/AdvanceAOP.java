/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月20日
 */
package cn.withmes.spring.cloud.hysrix.demo.controller.test;

import cn.withmes.spring.cloud.hysrix.demo.controller.davance.MyHystrixAnnotation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * ClassName: AdvanceAOP
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月20日
 */
@Aspect
@Component(value = "asdas")
public class AdvanceAOP {

    @Pointcut("execution(public * cn.withmes.spring.cloud.hysrix.demo..Test.*(..))")
    public void pointCutSay() {
    }

    @Around("pointCutSay() ")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object proceed = null;
        try {
            proceed = point.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            System.out.println("finally....");
        }
        return proceed;
    }

}
