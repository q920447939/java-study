/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年02月02日
 */

/**
 * ClassName: TestAllocation
 * @Description:
 * @author leegoo
 * @date 2023年02月02日
 */
public class TestAllocationOld {
    private static final int _1MB = 1024 * 1024;
    /**
     * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
     * -XX:PretenureSizeThreshold=3145728(3M)
     * -XX:+UseParNewGC (JDK8 必须指定收集器，否则不会直接分配到老年代)
     */
    public static void testAllocation() {
        byte[] allocation;
        allocation = new byte[4 * _1MB]; //直接分配在老年代中
    }

    public static void main(String[] args) {
        testAllocation();
    }
}
