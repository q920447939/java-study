/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月14日
 */
package cn.withme.gc;

import java.text.DecimalFormat;

/**
 * ClassName: GCTest01
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月14日
 */
public class GCTest01 {
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(8_000);
        byte[] bytes1 = new byte[1 * 1024 * 1024];
        System.out.println("分配1M内存");
        Thread.sleep(3_000);
        showJVMinfo();

        byte[] bytes4 = new byte[4 * 1024 * 1024];
        System.out.println("分配4M内存");
        showJVMinfo();

    }

    private static void showJVMinfo() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        System.out.println("最大内存:" + toM(maxMemory));

        long freeMemory = Runtime.getRuntime().freeMemory();
        System.out.println("剩余内存:" + toM(freeMemory));

        long totalMemory = Runtime.getRuntime().totalMemory();
        System.out.println("使用内存:" + toM(totalMemory));

    }


    static private String toM(long maxMemory) {
        float num = (float) maxMemory / (1024 * 1024);
        DecimalFormat df = new DecimalFormat("0.00");// 格式化小数
        String s = df.format(num);// 返回的是String类型
        return s;
    }


}
