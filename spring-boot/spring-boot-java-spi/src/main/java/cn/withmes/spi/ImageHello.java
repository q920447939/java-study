/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月04日
 */
package cn.withmes.spi;

/**
 * ClassName: ImageHello
 * @Description:
 * @author leegoo
 * @date 2019年07月04日
 */
public class ImageHello implements HelloInterface{
    @Override
    public void sayHello() {
        System.err.println("ImageHello");
    }
}
