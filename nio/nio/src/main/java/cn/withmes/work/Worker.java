/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月08日
 */
package cn.withmes.work;

/**
 苦逼工人工作
 */
public class Worker implements Job{
    @Override
    public void doJob(Handler handler) {
        System.out.println("工人开始工作");
    }
}
