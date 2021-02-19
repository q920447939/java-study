/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年12月09日
 */
package cn.withme.pattern.state;

/**
 * ClassName: State
 * @Description:
 * @author leegoo
 * @date 2020年12月09日
 */
public interface State {

    //插入硬币
    void insertQuarter();

    //吐出硬币
    void ejectQuarter();

    //转动机器
    void turnCrank();

    //发放糖果
    void dispense();
}
