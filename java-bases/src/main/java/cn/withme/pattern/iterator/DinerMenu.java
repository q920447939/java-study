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

import java.util.Arrays;

/**
 * ClassName: PancakeHouseMenu
 *
 * @author leegoo
 * @Description:
 * @date 2020年12月02日
 */
@Setter
@Getter
public class DinerMenu extends MenuItem {
     static  int MAX_ITEMS = 6;
     int numberOfItems = 0;
     MenuItem[] menuItems;



    DinerMenu() {
        menuItems = new MenuItem[MAX_ITEMS];
        this.buildMenus();
    }

     DinerMenu(String name, String description, boolean vegetation, double price) {
        this.name = name;
        this.description = description;
        this.vegetation = vegetation;
        this.price = price;
    }



    public void buildMenus() {
        if (numberOfItems >= MAX_ITEMS) return;

        menuItems[numberOfItems++] = new DinerMenu("小炒黄牛肉","红辣椒炒牛肉",false,89.9);
        menuItems[numberOfItems++] = new DinerMenu("西红柿炒鸡蛋","西红柿炒鸡蛋",false,25.9);
        menuItems[numberOfItems++] = new DinerMenu("空心菜","空心菜",false,15.9);
    }


    @Override
    public String toString() {
        return "DinerMenu{" +
                "numberOfItems=" + numberOfItems +
                ", menuItems=" + Arrays.toString(menuItems) +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", vegetation=" + vegetation +
                ", price=" + price +
                '}';
    }


}
