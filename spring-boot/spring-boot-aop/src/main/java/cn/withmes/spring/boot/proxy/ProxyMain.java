package cn.withmes.spring.boot.proxy;

import java.lang.reflect.Proxy;

public class ProxyMain
{
    public static void main(String[] args) {
        Request realClass= (Request) Proxy.newProxyInstance(ProxyMain.class.getClassLoader(),new Class[]{Request.class},new JDKproxy(new RealClass()));
        realClass.hello();
        realClass.sayName();
    }
}
