/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月14日
 */
package cn.withmes.spring.boot.proxy.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * ClassName: CglibInteceptor
 * @Description:
 * @author leegoo
 * @date 2019年05月14日
 */
public class CglibInteceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("intercept in....");
        Object o1 = methodProxy.invokeSuper(o, objects);
        System.out.println("intercept after...."+o1);
        return null;
    }
}
