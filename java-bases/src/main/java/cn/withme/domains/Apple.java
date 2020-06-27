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
 * ClassName: Apple
 * @Description:
 * @author leegoo
 * @date 2020年06月22日
 */
@Getter
@Setter
public class Apple {
    private String name = "苹果";
    private int weight = 80;

    @Override
    public String toString() {
        return "Apple{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                '}';
    }
}
