package cn.withme.pattern.abstractFactory.advance.factory;

import cn.withme.pattern.abstractFactory.advance.domain.ChangshaGreenPissa;
import cn.withme.pattern.abstractFactory.advance.domain.ChangshaRedPissa;
import cn.withme.pattern.abstractFactory.advance.domain.Pissa;
import cn.withme.pattern.abstractFactory.enums.LocationEnum;

/**
 * @className: ChangshaPissaFactory
 * @description:
 * @author: liming
 * @date: 2020/1/16
 **/
public class ChangshaPissaFactory extends PissaAbstractFactory{

    public static String LOCALTION = LocationEnum.CHANGSHA.getDesc();


    @Override
    Pissa create(String type) {
        Pissa p;
        switch (type) {
            case "green":
                p = new ChangshaGreenPissa();
                break;
            case "red":
                p = new ChangshaRedPissa();
                break;
            //省略n个case
            default:
                p = new Pissa();
        }
        return p;
    }
}
