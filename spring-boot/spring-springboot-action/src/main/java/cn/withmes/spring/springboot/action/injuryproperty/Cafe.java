/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月30日
 */
package cn.withmes.spring.springboot.action.injuryproperty;

/**
 * ClassName: Cafe
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月30日
 */

public class Cafe {

    private  String color;

    private Integer weight;


    public Cafe(String color, Integer weight) {
        this.color = color;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Cafe{" +
                "color='" + color + '\'' +
                ", weight=" + weight +
                '}';
    }
}
