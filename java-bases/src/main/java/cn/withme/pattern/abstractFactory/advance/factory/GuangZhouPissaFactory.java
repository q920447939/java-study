package cn.withme.pattern.abstractFactory.advance.factory;

import cn.withme.pattern.abstractFactory.advance.domain.GuangzhouGreenPissa;
import cn.withme.pattern.abstractFactory.advance.domain.GuangzhouRedPissa;
import cn.withme.pattern.abstractFactory.advance.domain.Pissa;
import cn.withme.pattern.abstractFactory.enums.LocationEnum;

/**
 * @className: ChangshaPissaFactory
 * @description:
 * @author: liming
 * @date: 2020/1/16
 **/
public class GuangZhouPissaFactory extends PissaAbstractFactory{

    public static String LOCALTION = LocationEnum.GUANGZHOU.getDesc();

    @Override
    Pissa create(String type) {
        Pissa p;
        switch (type) {
            case "green":
                p = new GuangzhouGreenPissa();
                break;
            case "red":
                p = new GuangzhouRedPissa();
                break;
            //省略n个case
            default:
                p = new Pissa();
        }
        return p;
    }
}
