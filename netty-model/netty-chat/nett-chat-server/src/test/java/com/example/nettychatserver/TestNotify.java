/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年04月18日
 */
package com.example.nettychatserver;

/**
 * ClassName: TestNotify
 * @Description:
 * @author leegoo
 * @date 2023年04月18日
 */
public class TestNotify {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("开始");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("结束");
        });
    }
}
