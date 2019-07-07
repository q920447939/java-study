/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月07日
 */
package cn.withmes.provider;

/**
 * ClassName: DemoServiceImpl
 * @Description:
 * @author leegoo
 * @date 2019年07月07日
 */
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        return "这是服务端,发送的名字为: " + name;
    }
}