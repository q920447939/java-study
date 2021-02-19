/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年12月07日
 */
package cn.withme.pattern.iterator.impl;

import cn.withme.pattern.iterator.MenuItem;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ClassName: WaiterItrator
 * @Description:
 * @author leegoo
 * @date 2020年12月07日
 */
public class WaiterIterator {
    private  MenuItem[] menuItems;
    private  List<MenuItem> pancakeHouseMenus;

    public WaiterIterator(MenuItem[] menuItems) {
        this.menuItems = menuItems;
    }

    public WaiterIterator(List<MenuItem> pancakeHouseMenus) {
        this.pancakeHouseMenus = pancakeHouseMenus;
    }

    public Iterator<MenuItem> createIterator(){
        return Stream.of(menuItems).collect(Collectors.toList()).iterator();
    }
}
