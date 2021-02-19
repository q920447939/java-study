/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年12月09日
 */
package cn.withme.pattern.state;

import cn.withme.pattern.state.impl.HasQuarterStateImpl;
import cn.withme.pattern.state.impl.NoQuarterStateImpl;
import cn.withme.pattern.state.impl.SoldOutStateImpl;
import cn.withme.pattern.state.impl.SoldStateImpl;

/**
 * ClassName: GumballMachine
 * @Description:
 * @author leegoo
 * @date 2020年12月09日
 */
public class GumballMachine {

    int count = 0; //当前糖果机数量
    State noQuarterState;
    State hasQuarterState;
    State soldOutState;
    State soldState;
    State state ;

    public GumballMachine(int count) {
        this.count = count;
        this.noQuarterState = new NoQuarterStateImpl(this);
        this.hasQuarterState = new HasQuarterStateImpl(this);
        this.soldOutState = new SoldOutStateImpl(this);
        this.soldState = new SoldStateImpl(this);
        state = this.noQuarterState;
    }

    /***
     *
     * 1.投入硬币
     *      <25  退回硬币
     *      > 25
     *          检查糖果机数量
     *              <= 0  数量不足,退回硬币
     *              > 0  数量-1,
     *
     *
     */
    public void putInCoin (int coin ,int count) {

    }

    //发放糖果
    public void releaseBall(){
        System.out.println("给用户发放一颗糖果");
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setState(State state){
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public State getNoQuarterState() {
        return noQuarterState;
    }

    public void setNoQuarterState(State noQuarterState) {
        this.noQuarterState = noQuarterState;
    }

    public State getHasQuarterState() {
        return hasQuarterState;
    }

    public void setHasQuarterState(State hasQuarterState) {
        this.hasQuarterState = hasQuarterState;
    }

    public State getSoldOutState() {
        return soldOutState;
    }

    public void setSoldOutState(State soldOutState) {
        this.soldOutState = soldOutState;
    }

    public State getSoldState() {
        return soldState;
    }

    public void setSoldState(State soldState) {
        this.soldState = soldState;
    }

    public void insertQuarter() {
        state.insertQuarter();
    }

    public void ejectQuarter() {
        state.ejectQuarter();
    }

    public void turnCrank() {
        state.turnCrank();
    }

    public void dispense() {
        state.dispense();
    }
}
