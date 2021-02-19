/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年12月09日
 */
package cn.withme.pattern.state.impl;

import cn.withme.pattern.state.GumballMachine;
import cn.withme.pattern.state.State;

/**
 * ClassName: 卖的状态
 * @Description:
 * @author leegoo
 * @date 2020年12月09日
 */
public class SoldStateImpl implements State {

    private GumballMachine machine;

    public SoldStateImpl(GumballMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("请等待上一颗糖果发放完成");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("正在准备糖果,不能弹出硬币");
    }

    @Override
    public void turnCrank() {
        System.out.println("正在准备糖果,不能再次转动糖果机");

    }

    @Override
    public void dispense() {
        if (machine.getCount() <= 0) {
            System.out.println("当前糖果机数量不足");
            machine.setState(machine.getNoQuarterState());
            return;
        }
        machine.setCount(machine.getCount()-1);
        machine.setState(machine.getSoldOutState());
        System.out.println("发放糖果完成");
    }
}
