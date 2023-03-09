/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月08日
 */
package cn.withmes.work;


/**
 * 工作助手
 */
public class Handler {
    private Job job;

    public void setJob(Job job) {
        this.job = job;
    }

    public void doJob(){
        this.job.doJob(this);
    }
}
