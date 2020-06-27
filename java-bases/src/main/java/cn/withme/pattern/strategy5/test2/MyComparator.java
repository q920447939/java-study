/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月26日
 */
package cn.withme.pattern.strategy5.test2;

/**
 * ClassName: MyComparator
 * @Description: 比较器
 * @author leegoo
 * @date 2020年06月26日
 */
public interface MyComparator<T> {
    int sort (T t1,T t2);
}
