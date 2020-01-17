package cn.withme.pattern.abstractFactory.simple;

import java.util.HashMap;
import java.util.Map;

/**
 * @className: PissaFactory
 * @description: 简单的披萨生产工厂
 * @author: liming
 * @date: 2020/1/16
 **/
public class PissaFactory {

    private static final Map<String,Pissa> pissaMap = new HashMap<>();

    static {
        pissaMap.put("green", new GreenPissa());
        pissaMap.put("red", new RedPissa());
    }

    public Pissa create(String type) {
        Pissa p;
        switch (type) {
            case "green":
                p = new GreenPissa();
                break;
            case "red":
                p = new RedPissa();
                break;
             //省略n个case
            default:
                p = new Pissa();
        }
        return p;
    }

    public Pissa create2(String type) {
       return pissaMap.get(type);
    }

}
