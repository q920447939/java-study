/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月06日
 */
package cn.withmes.spring.cloud.eureka.server.controller;

import cn.withmes.spring.cloud.eureka.api.SayServer;
import cn.withmes.spring.cloud.eureka.api.UserService;
import cn.withmes.spring.cloud.eureka.api.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: SimpleController
 *
 * @author leegoo
 * @Description:
 * @date 2019年07月06日
 */
@RestController
public class SayController implements SayServer, EnvironmentAware {

    private Environment environment;


    @Override
    public String sayInfo() {
        System.err.println("sayinfo.....");

        String port = this.environment.getProperty("server.port");

        return "当前服务器端口为:" + port;
    }

    @Override
    public User save(User user) {
        user.setId(new Random().nextInt());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return user;
    }

    @Override
    public String getMessageById(String id) {
        int sleepTime = new Random().nextInt(10);
        System.out.println("服务器查询,一共耗时:"+sleepTime+"秒");
        try {
            TimeUnit.SECONDS.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "服务器查询到信息,信息内容为: 你好,2008年奥运会已经结束 ,id:"+id;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
