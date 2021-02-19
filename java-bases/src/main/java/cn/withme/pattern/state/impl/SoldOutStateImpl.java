/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年12月09日
 */
package cn.withme.pattern.state.impl;

import cn.withme.pattern.state.GumballMachine;
import cn.withme.pattern.state.State;

/**
 * ClassName: 卖出状态
 * @Description:
 * @author leegoo
 * @date 2020年12月09日
 */
public class SoldOutStateImpl implements State {

    private GumballMachine machine;

    public SoldOutStateImpl(GumballMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("请等待,我们已经给你准备了糖果");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("对不起,已经发放过糖果,不能再弹出硬币");
    }

    @Override
    public void turnCrank() {
        System.out.println("多次转动糖果机不能获得任何糖果");

    }

    @Override
    public void dispense() {
        System.out.println("已经发放糖果了,不能再发..");
    }
}
