/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月22日
 */
package cn.withme.domains;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * ClassName: Orange
 * @Description:
 * @author leegoo
 * @date 2020年06月22日
 */
@Getter
@Setter
public class Orange {

    private String name = "橘子";
    private int weight = 70;

    @Override
    public String toString() {
        return "Orange{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                '}';
    }
}
