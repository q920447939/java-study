/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月21日
 */
package cn.withme.genericity;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ClassName: Fruit
 * @Description:
 * @author leegoo
 * @date 2020年06月21日
 */
public class Fruit {

    private static List<Fruit> items = new ArrayList<>();


    public static void main(String[] args) {
        List<? extends  Fruit> list = new ArrayList<>();
        List<? super   Fruit> list2 = new ArrayList<>();
        list2.add(new Apple());

        Object object = list2.get(1);
    }

}


class Apple extends  Fruit {

}


class Banana extends  Fruit {

}
