/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年12月02日
 */
package cn.withme.pattern.iterator.impl;

import cn.withme.pattern.iterator.PancakeHouseMenu;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * ClassName: HouseIterator
 * @Description:
 * @author leegoo
 * @date 2020年12月02日
 */
public class HouseIterator implements Iterator<PancakeHouseMenu> {
    volatile List<PancakeHouseMenu> pancakeHouseMenus;

     volatile int CUR_IDX = 0;

     PancakeHouseMenu curItem ;

    public HouseIterator(List<PancakeHouseMenu> pancakeHouseMenus) {
        this.pancakeHouseMenus = pancakeHouseMenus;
    }

    @Override
    public boolean hasNext() {
        if (CollectionUtils.isEmpty(pancakeHouseMenus) || CUR_IDX >= pancakeHouseMenus.size()){
            curItem = null;
            return false;
        }
        curItem = pancakeHouseMenus.get(CUR_IDX++);
        return null != curItem;
    }

    @Override
    public PancakeHouseMenu next() {
        return curItem;
    }

    @Override
    public void remove() {

    }

    @Override
    public void forEachRemaining(Consumer<? super PancakeHouseMenu> action) {

    }
}
