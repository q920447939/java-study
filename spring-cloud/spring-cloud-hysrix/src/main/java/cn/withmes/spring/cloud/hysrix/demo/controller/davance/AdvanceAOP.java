/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月20日
 */
package cn.withmes.spring.cloud.hysrix.demo.controller.davance;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
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
@Component
public class AdvanceAOP {


    private ExecutorService executorService = Executors.newFixedThreadPool(20);


    @Pointcut("execution(public * cn.withmes.spring.cloud.hysrix.demo..AdvanceController.*Method(..))")
    public void pointCutSay() {

    }

    @Pointcut("@annotation(MyHystrixAnnotation)")
    public void pointCutMyHystrixAnnotation() {

    }


    @Around("pointCutSay()&& args(time) ")
    public Object around(ProceedingJoinPoint point, long time) throws Throwable {
        return hystrix(point, time);
    }


    @Around("pointCutMyHystrixAnnotation() ")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Field field = point.getClass().getDeclaredField("methodInvocation");
        field.setAccessible(true); // 设置些属性是可以访问的
        //通过反射拿到注解上面的值
        long time = ((ProxyMethodInvocation) field.get(point)).getMethod().getAnnotation(MyHystrixAnnotation.class).timeout();
        return hystrix(point, time);


    }

    private Object hystrix(ProceedingJoinPoint point, long time) throws Throwable {
        //methodInvocation
        Future<?> future = executorService.submit(() -> {
            try {
                point.proceed(new Object[]{time});
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        try {
            future.get(200, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ex) {
            future.cancel(true);
            System.err.println("超时了....");
            return "超时了....";
        }
        return point.proceed(new Object[]{time});
    }

}
