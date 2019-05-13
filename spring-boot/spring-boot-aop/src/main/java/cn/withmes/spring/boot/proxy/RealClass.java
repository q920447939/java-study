package cn.withmes.spring.boot.proxy;

public class RealClass implements  Request
{
    @Override
    public void hello() {
        System.out.println("real hello");
    }

    @Override
    public void sayName() {
        System.out.println("My name is Jack");
    }
}
