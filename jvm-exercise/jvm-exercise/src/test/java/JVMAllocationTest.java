/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年02月02日
 */

import org.junit.Test;

/**
 * ClassName: JVMAllocationTest
 *
 * @author leegoo
 * @Description:
 * @date 2023年02月02日
 */
public class JVMAllocationTest {

    private static final int _1MB = 1024 * 1024;

    /**
     * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1
     * -XX:+PrintTenuringDistribution
     */
    @SuppressWarnings("unused")
    @Test
    public void testTenuringThreshold() {
        byte[] allocation1, allocation2, allocation3;
        allocation1 = new byte[_1MB / 4]; // 什么时候进入老年代决定于XX:MaxTenuring-Threshold设置
        allocation2 = new byte[4 * _1MB];
        allocation3 = new byte[4 * _1MB];
        allocation3 = null;
        allocation3 = new byte[4 * _1MB];
    }


    /**
     * 动态对象年龄判定
     * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
     -XX:MaxTenuringThreshold=15
     * -XX:+PrintTenuringDistribution
     *
     * 不注释 allocation2 = new byte[_1MB / 4]; 这一行 ，  新生代 from to  t区内存被回收， 老年代占用比例增大（可能后面又触发了一次Full GC ,导致老年代占用比例减少）
     * allocation1、allocation2 两个对象加起来已经到达了512KB，
     * 并且它们是同年龄的，满足同年对象达到Survivor空间一半的规则。我们只要注释掉其中一个对象的
     * new操作，就会发现另外一个就不会晋升到老年代了。
     *
     * 注释 allocation2 = new byte[_1MB / 4]; 这一行 ， 新生代 from to 区有内存占用，老年代比例占用变大（因为新生代存放了一些内存，可能导致老年代的占用比例变高（讨论Full GC, 没有被Full GC））
     *
     */
    @SuppressWarnings("unused")
    @Test
    public  void testTenuringThreshold2() {
        byte[] allocation1, allocation2, allocation3, allocation4;
        allocation1 = new byte[_1MB / 4]; // allocation1+allocation2大于survivo空间一半
        allocation2 = new byte[_1MB / 4];
        allocation3 = new byte[4 * _1MB];
        allocation4 = new byte[4 * _1MB];
        allocation4 = null;
        allocation4 = new byte[4 * _1MB];
    }
}
