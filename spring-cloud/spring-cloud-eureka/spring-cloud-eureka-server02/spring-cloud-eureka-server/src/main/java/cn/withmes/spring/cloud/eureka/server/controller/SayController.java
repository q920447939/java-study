/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月06日
 */
package cn.withmes.spring.cloud.eureka.server.controller;

import cn.withmes.spring.cloud.eureka.api.SayServer;
import cn.withmes.spring.cloud.eureka.api.pojo.User;
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
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
