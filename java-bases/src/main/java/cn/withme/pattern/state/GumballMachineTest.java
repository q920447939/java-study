/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年12月09日
 */
package cn.withme.pattern.state;

/**
 * ClassName: GumballMachineTest
 * @Description:
 * @author leegoo
 * @date 2020年12月09日
 */
public class GumballMachineTest {

    public static void main(String[] args) {
        GumballMachine gumballMachine = new GumballMachine(1);
        gumballMachine.dispense();
        
        ///
        gumballMachine.insertQuarter();
        gumballMachine.turnCrank();
        gumballMachine.dispense();
        gumballMachine.dispense();
    }
}
