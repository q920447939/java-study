/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月04日
 */
package cn.withmes.spi;

/**
 * ClassName: TextHello
 * @Description:
 * @author leegoo
 * @date 2019年07月04日
 */
public class TextHello implements  HelloInterface {
    @Override
    public void sayHello() {
        System.err.println("TextHello");
    }
}
