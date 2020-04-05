/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月20日
 */
package cn.withme.pattern.strategy2;

/**
 * ClassName: Pay
 * @Description:
 * @author leegoo
 * @date 2020年03月20日
 */
public interface Pay<T> {
    Result<T> pay(T t);
}
