package cn.withme.pattern.abstractFactory.advance.factory;

import cn.withme.pattern.abstractFactory.advance.domain.Pissa;

/**
 * @className: PissaAbstractFactory
 * @description: 抽象披萨工厂
 * @author: liming
 * @date: 2020/1/16
 **/
public abstract class PissaAbstractFactory {
    abstract Pissa create(String type);
}
