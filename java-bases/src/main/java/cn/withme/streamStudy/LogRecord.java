/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年07月23日
 */
package cn.withme.streamStudy;

/**
 * ClassName: LogRecord
 * @Description:
 * @author leegoo
 * @date 2021年07月23日
 */
public class LogRecord implements Record{

    @Override
    public void before() {
        System.out.println("record log start....");
    }

    @Override
    public void after() {
        System.out.println("record log after....");
    }
}
