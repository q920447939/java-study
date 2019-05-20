package cn.withmes.spring.boot.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JDKproxy implements InvocationHandler {

    private  RealClass realClass;

    public JDKproxy(RealClass realClass) {
        this.realClass = realClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before invoke");
        method.invoke(realClass,args);
        System.out.println("after invoke");
        return null;
    }
}
