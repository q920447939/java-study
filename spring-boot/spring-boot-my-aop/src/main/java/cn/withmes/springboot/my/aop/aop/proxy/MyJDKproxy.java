package cn.withmes.springboot.my.aop.aop.proxy;

import cn.withmes.springboot.my.aop.aop.scan.MyTransaction;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MyJDKproxy implements InvocationHandler {

    private MyTransaction ma;
    private  Class<?> aClass;
    private final BeanFactory beanFactory;

    public MyJDKproxy(Class<?> aClass , MyTransaction ma, BeanFactory beanFactory) {
        this.ma = ma;
        this.aClass = aClass;
        this.beanFactory = beanFactory;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            String simpleName = aClass.getSimpleName();
            String first = String.valueOf(simpleName.charAt(0));
            String s1 = simpleName.replaceFirst(first, first.toLowerCase());
            method.invoke(beanFactory.getBean(s1),args);
        } catch (Exception e) {
            System.out.println("发生exception 异常.开始调用callback方法"+ Arrays.toString(args));
            Class<?> aClass = ma.callBackClass();
            Object instance = null;
            try {
                instance = aClass.newInstance();
                Class<?>[] pars = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    pars[i] =  Class.forName(args[i].getClass().getTypeName());
                }
                Method callBackMehtod = aClass.getMethod(ma.callBackMethod(), pars);
                callBackMehtod.invoke(instance,args);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("结束调用");
        System.out.println("==================");
        return null;
    }

}
