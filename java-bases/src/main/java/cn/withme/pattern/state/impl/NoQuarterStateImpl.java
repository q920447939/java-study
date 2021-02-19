/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年12月09日
 */
package cn.withme.pattern.state.impl;

import cn.withme.pattern.state.GumballMachine;
import cn.withme.pattern.state.State;

/**
 * ClassName: 数量不足状态
 * @Description:
 * @author leegoo
 * @date 2020年12月09日
 */
public class NoQuarterStateImpl implements State {

    private GumballMachine machine;

    public NoQuarterStateImpl(GumballMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertQuarter() {
        machine.setState(machine.getHasQuarterState());
        System.out.println("投入硬币成功!");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("你没有投入硬币,不能弹出硬币");
    }

    @Override
    public void turnCrank() {
        System.out.println("硬币不足,不能转动糖果机");
    }

    @Override
    public void dispense() {
        System.out.println("硬币不足,不能发糖果..");
    }
}
