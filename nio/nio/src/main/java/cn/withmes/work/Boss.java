/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月08日
 */
package cn.withmes.work;


/**
 包工头工作
 */
public class Boss implements Job {

    @Override
    public void doJob(Handler handler) {
        System.out.println("包工头开始工作");
        handler.setJob(new Worker());
    }
}
