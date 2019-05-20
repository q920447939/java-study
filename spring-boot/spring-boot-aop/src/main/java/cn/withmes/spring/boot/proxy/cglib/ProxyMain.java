package cn.withmes.spring.boot.proxy.cglib;

import cn.withmes.spring.boot.proxy.jdk.RealClass;
import org.springframework.cglib.proxy.Enhancer;

public class ProxyMain {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(RealClass.class);
        enhancer.setCallback(new CglibInteceptor());
        RealClass realClass = (RealClass) enhancer.create();
        realClass.hello();
        realClass.sayName();
    }
}
