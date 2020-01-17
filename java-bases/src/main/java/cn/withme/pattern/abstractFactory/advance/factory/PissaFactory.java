package cn.withme.pattern.abstractFactory.advance.factory;


import cn.withme.pattern.abstractFactory.advance.domain.Pissa;

/**
 * @className: PissaFactory
 * @description: 简单的披萨生产工厂
 * @author: liming
 * @date: 2020/1/16
 **/
public class PissaFactory {

    private PissaAbstractFactory abstractFactory;

    public PissaFactory(PissaAbstractFactory abstractFactory) {
        this.abstractFactory = abstractFactory;
    }

    public Pissa create(String type) {
        Pissa pissa = abstractFactory.create(type);
        return pissa;
    }
}
