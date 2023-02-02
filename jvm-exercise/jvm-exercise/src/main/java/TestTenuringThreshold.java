/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年02月02日
 */

import org.junit.Test;

/**
 * ClassName: TestTenuringThreshold
 * @Description:
 * @author leegoo
 * @date 2023年02月02日
 */
public class TestTenuringThreshold {
    private static final int _1MB = 1024 * 1024;

    /**
     * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1
     * -XX:+PrintTenuringDistribution
     * 改变 MaxTenuringThreshold 的值无效，未实验成功
     *  在采用JDK8默认的GC时  新生代 from 和to 被回收了
     *  在增加 -XX:+UseParNewGC 参数后， 新生代 from 和to 没有被回收
     *
     */
    @SuppressWarnings("unused")
    public static void testTenuringThreshold() {
        byte[] allocation1, allocation2, allocation3;
        allocation1 = new byte[_1MB / 4]; // 什么时候进入老年代决定于XX:MaxTenuring-Threshold设置
        allocation2 = new byte[4 * _1MB];
        allocation3 = new byte[4 * _1MB];
        allocation3 = null;
    }

    public static void main(String[] args) {
        testTenuringThreshold();
    }
}
