package cn.withme.pattern.abstractFactory.simple;

/**
 * @className: SimplePissaTest
 * @description: 披萨测试
 * @author: liming
 * @date: 2020/1/16
 **/
public class SimplePissaTest {

    public static void main(String[] args) {
        PissaFactory factory = new PissaFactory();
        Pissa pissa = factory.create("red");
        print(pissa);

        System.out.println("==============");
        Pissa pissa1 = factory.create2("red");
        print(pissa1);
    }

    private static void print(Pissa pissa) {
        System.out.println(null == pissa ? "" :pissa.getDesc());
    }
}
