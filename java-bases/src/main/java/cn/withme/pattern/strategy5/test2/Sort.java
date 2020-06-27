/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月26日
 */
package cn.withme.pattern.strategy5.test2;

import cn.withme.pattern.strategy5.test2.domain.BaseExPressCompany;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * ClassName: Sort
 * @Description:
 * @author leegoo
 * @date 2020年06月26日
 */
public abstract class Sort<T> {
    public abstract List<T> sortBySpeed(Class<?> target,List<T> list, MyComparator<T> comparator) ;


}
