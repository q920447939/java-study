/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月04日
 */
package cn.withmes.spi;

import java.util.ServiceLoader;

/**
 * ClassName: SpiTest
 * @Description:
 * @author leegoo
 * @date 2019年07月04日
 */
public class SpiTest {

    public static void main(String[] args) {
        ServiceLoader<HelloInterface> load = ServiceLoader.load(HelloInterface.class);
        load.forEach(HelloInterface::sayHello);
    }
}
