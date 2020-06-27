/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月27日
 */
package cn.withme.pattern.strategy5.test2;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ClassName: MySort
 * @Description:
 * @author leegoo
 * @date 2020年06月27日
 */
public class MySort<T> extends  Sort<T> {

    private Class<?> clazz ;

/*    @SuppressWarnings("unchecked")
    public MySort() {
        @SuppressWarnings("rawtypes")
        Class clazz = getClass();
        while (clazz != Object.class) {
            Type t = clazz.getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) t).getActualTypeArguments();
                if (args[0] instanceof Class) {
                    this.clazz = (Class<T>) args[0];
                    break;
                }
            }
            this.clazz = clazz.getSuperclass();
        }
    }*/

    @Override
    public List<T> sortBySpeed(Class<?> target,List<T> list, MyComparator<T> comparator) {
        T[] array = toArray(target,list);
        for (int i = 0; i < array.length; i++) {
            for (int j = i; j < array.length; j++) {
                if (comparator.sort(array[j],array[i])>= 1){
                    swap(array,i,j);
                }
            }
        }
        list = Arrays.asList(array);
        return  list;
    }

    public  void swap (T [] arr ,int i ,int j){
        T tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Class<?> cls, List<T> items) {
        if (items == null || items.size() == 0) {
            return (T[]) Array.newInstance(cls, 0);
        }
        return items.toArray((T[]) Array.newInstance(cls, items.size()));
    }

}
