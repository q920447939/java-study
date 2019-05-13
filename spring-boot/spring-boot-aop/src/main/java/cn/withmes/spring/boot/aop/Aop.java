/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月13日
 */
package cn.withmes.spring.boot.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
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


    /**
     * 注解织入
     */
    @Pointcut("@annotation(MyAopAnno)")
    public void pointcut(){
        System.out.println("pointcut...");
    }

    /*
    @Before(value = "@annotation(MyAopAnno)")
    public void before () {
        System.out.println("before...");
        service.checkUser();
    }*/

    /**
     *           访问修饰符   返回类型  包名                        尾号Service结尾的类   任意方法           任意参数
     * execution(public     *         cn.withmes.spring.boot.aop.*Sevice.            *         (..)    )
     */
   @Pointcut("execution(public * cn.withmes.spring.boot.aop.*Sevice.*(..))")
   public void matchCondition(){

   }

    /**
     *           访问修饰符   返回类型  包名                        尾号Service结尾的类   指定方法           任意参数
     * execution(public     *         cn.withmes.spring.boot.aop.*Sevice.            findUserB         (..)    )
     */
    @Pointcut("execution(public * cn.withmes.spring.boot.aop.*Sevice.findUserB(..))")
    public void matchCondition2(){

    }



    /**
     *           访问修饰符   返回类型  包名尾号Service结尾的类(包括子包,最后是两个..)   任意方法           任意参数
     * execution(public     *         cn.withmes.spring.boot.aop..*Service.        *                (..)    )
     */
    @Pointcut("execution(public * cn.withmes.spring.boot.aop..*Service.*(..))")
    public void matchCondition3(){

    }

    /**
     *           访问修饰符   返回类型  包名尾号Service结尾的类(包括子包,最后是两个..)   任意方法           任意参数   抛出异常   指定异常
     * execution(public     *         cn.withmes.spring.boot.aop..*Service.        *                (..)      throws    java.lang.Exception       )
     */
    @Pointcut("execution(public * cn.withmes.spring.boot.aop..*Service.*(..) throws java.lang.Exception)")
    public void matchCondition4(){

    }



    /**
     *          测试获取参数
     * execution(public     *         cn.withmes.spring.boot.aop..*Service.        *                (..))
     */
    @Pointcut("execution(public * cn.withmes.spring.boot.aop..*Service.*(..))")
    public void executionGetParams(){

    }


    /**
     * matchCondition2()  自定义切面规则,在切面之前做什么事情
     */
   @Before("executionGetParams() && args(user)")
   public void before (User user) {
       System.out.println("before execution...." +user);
   }

    /**
     *  @Around是代码执行之后的逻辑  try 里面可以获取到result  finally 可以获取到返回之后继续执行某些代码  catch里面可以在方法失败之后执行某些代码
     * @param point
     * @return
     */
   @Around(value = "@annotation(MyAopAnno)")
   public Object after(ProceedingJoinPoint point) {
       Object result = null;
       try {
           result =  point.proceed(point.getArgs());
           System.out.println("result:"+result);
       } catch (Throwable throwable) {
           throwable.printStackTrace();
           System.out.println("catch:"+throwable);
       } finally {
           System.out.println("finally:");
       }
       return result;
   }
}
