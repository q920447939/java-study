/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月08日
 */
package cn.withmes.work;

/**
 * 工作接口。指派人工
 */
public interface Job {
    /**
     * 做工作
     * @param handler 工作助手
     */
    void doJob(Handler handler);
}
