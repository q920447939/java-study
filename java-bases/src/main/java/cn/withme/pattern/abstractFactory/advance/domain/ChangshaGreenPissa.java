package cn.withme.pattern.abstractFactory.advance.domain;

import cn.withme.pattern.abstractFactory.advance.factory.ChangshaPissaFactory;
import cn.withme.pattern.abstractFactory.enums.ColorEnum;
import lombok.Data;

import java.util.Arrays;

/**
 * @className: GreenPissa
 * @description: 绿色披萨
 * @author: liming
 * @date: 2020/1/16
 **/
@Data
public class ChangshaGreenPissa extends  Pissa{

    public ChangshaGreenPissa() {
        this.colors = Arrays.asList(ColorEnum.GREEN.getDesc());
        this.name = "一个包含"+this.colors+"的披萨";
        this.price = 10.0;
    }

    @Override
    public String getDesc () {
        return this.name+",\t 价格:"+this.price+",\t 生产地点位于:" + ChangshaPissaFactory.LOCALTION;
    }
}
