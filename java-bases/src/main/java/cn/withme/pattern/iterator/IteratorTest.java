/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年12月02日
 */
package cn.withme.pattern.iterator;


import cn.withme.pattern.iterator.impl.WaiterIterator;

import java.util.Iterator;

/**
 * ClassName: IteratorTest
 *
 * @author leegoo
 * @Description:
 * @date 2020年12月02日
 */
public class IteratorTest {

    public static void main(String[] args) {
        DinerMenu dinerMenu = new DinerMenu();
        dinerMenu.buildMenus();

        PancakeHouseMenu pancakeHouseMenu = new PancakeHouseMenu();
        pancakeHouseMenu.buildMenus();

        WaiterIterator waiterIterator = new WaiterIterator(dinerMenu.getMenuItems());
        Iterator<MenuItem> iterator = waiterIterator.createIterator();
        while (iterator.hasNext()){
            MenuItem next = iterator.next();
            System.out.println("while...."+next);
        }
    }

}
