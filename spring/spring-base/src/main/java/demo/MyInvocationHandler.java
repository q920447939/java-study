/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年04月06日
 */
package demo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ClassName: MyInvocationHandler
 * @Description:
 * @author leegoo
 * @date 2020年04月06日
 */
public class MyInvocationHandler implements InvocationHandler {

    private Object target; //被代理对象的引用作为一个成员变量保存下来了

    public MyInvocationHandler(Object target) {
        this.target = target;
    }

    //获取被代理人的个人资料
    public Object getInstance(Object target) throws Exception{
        this.target = target;
        Class clazz = target.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equalsIgnoreCase("getName")){
            return "你牛逼啥子?";
        }
        return method.invoke(this.target, args);
    }
}
