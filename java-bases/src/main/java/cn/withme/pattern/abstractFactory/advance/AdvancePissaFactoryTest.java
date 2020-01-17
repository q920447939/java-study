package cn.withme.pattern.abstractFactory.advance;

import cn.withme.pattern.abstractFactory.advance.domain.Pissa;
import cn.withme.pattern.abstractFactory.advance.factory.GuangZhouPissaFactory;
import cn.withme.pattern.abstractFactory.advance.factory.PissaFactory;

/**
 * @className: AdvancePissaFactory
 * @description: 测试类
 * @author: liming
 * @date: 2020/1/16
 **/
public class AdvancePissaFactoryTest {

    public static void main(String[] args) {
        PissaFactory pissaFactory = new PissaFactory(new GuangZhouPissaFactory());
        Pissa pissa = pissaFactory.create("red");
        System.out.println(pissa.getDesc());
    }
}
