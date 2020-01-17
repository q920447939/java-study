package cn.withme.pattern.abstractFactory.simple;

import lombok.Data;

import java.util.List;

/**
 * @className: PissaFactory
 * @description: 简单的披萨生产工厂
 * @author: liming
 * @date: 2020/1/16
 **/
@Data
public class Pissa {
    protected String name ;
    protected List<String> colors;
    protected Double price;

    public String getDesc () {
        return null;
    }
}
