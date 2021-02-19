/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年12月02日
 */
package cn.withme.pattern.iterator;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ClassName: MenuItem
 * @Description:
 * @author leegoo
 * @date 2020年12月02日
 */
@Setter
@Getter
@ToString
public class MenuItem {
    String name;
    String description;
    boolean vegetation;
    double price;



}
