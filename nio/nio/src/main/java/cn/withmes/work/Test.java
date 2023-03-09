/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月08日
 */
package cn.withmes.work;

/**
 * ClassName: Test
 *
 * @author leegoo
 * @Description:
 * @date 2023年03月08日
 */
public class Test {

    public static void main(String[] args) {
        Handler handler = new Handler();
        handler.setJob(new Boss());
        handler.doJob();
        handler.doJob();
    }
}
