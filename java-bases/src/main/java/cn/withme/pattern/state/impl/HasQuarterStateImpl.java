/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年12月09日
 */
package cn.withme.pattern.state.impl;

import cn.withme.pattern.state.GumballMachine;
import cn.withme.pattern.state.State;

/**
 * ClassName: 数量满足状态
 * @Description:
 * @author leegoo
 * @date 2020年12月09日
 */
public class HasQuarterStateImpl implements State {

    private GumballMachine machine;

    public HasQuarterStateImpl(GumballMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("已经插入一个硬币了,不能再次插入硬币");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("弹出硬币...");
        //转向数量不足状态
        machine.setState(machine.getNoQuarterState());
    }

    @Override
    public void turnCrank() {
        System.out.println("请等待,我们正在准备糖果");
        machine.setState(machine.getSoldState());
    }

    @Override
    public void dispense() {
        System.out.println("检查数量完毕,此状态不能直接发放糖果");
    }
}
