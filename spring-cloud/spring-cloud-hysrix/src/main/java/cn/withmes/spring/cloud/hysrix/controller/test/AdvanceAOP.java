/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月20日
 */
package cn.withmes.spring.cloud.hysrix.controller.test;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

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
