/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月13日
 */
package cn.withmes.spring.boot.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ClassName: Aop
 * @Description:
 * @author leegoo
 * @date 2019年05月13日
 */

@Aspect
@Component
public class Aop {

    @Autowired
    private CheckUserService service;

   /* @Pointcut("@annotation(MyAopAnno)")
    public void pointcut(){
        System.out.println("pointcut...");
    }

    @Before(value = "@annotation(MyAopAnno)")
    public void before () {
        System.out.println("before...");
        service.checkUser();
    }*/

   @Pointcut("execution(public * cn.withmes.spring.boot.aop.*Sevice.*(..))")
   public void matchCondition(){

   }

   @Before("matchCondition()")
   public void before () {
       System.out.println("before ....");
   }
}
