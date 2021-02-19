/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年12月02日
 */
package cn.withme.pattern.iterator.impl;

import cn.withme.pattern.iterator.DinerMenu;
import cn.withme.pattern.iterator.MenuItem;
import cn.withme.pattern.iterator.PancakeHouseMenu;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * ClassName: HouseIterator
 * @Description:
 * @author leegoo
 * @date 2020年12月02日
 */
public class DinnerIterator implements Iterator<MenuItem> {
    volatile MenuItem[] dinerMenus ;

    volatile int CUR_IDX = 0;

      MenuItem curItem;

    public DinnerIterator(MenuItem[] dinerMenus ) {
        this.dinerMenus = dinerMenus;
    }

    @Override
    public boolean hasNext() {
        if (null == dinerMenus || CUR_IDX >= dinerMenus.length){
            curItem = null;
            return false;
        }
        curItem = dinerMenus[CUR_IDX++];
        return null != curItem;
    }

    @Override
    public MenuItem next() {
        return curItem;
    }

    @Override
    public void remove() {

    }

    @Override
    public void forEachRemaining(Consumer<? super MenuItem> action) {

    }
}
