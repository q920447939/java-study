/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月06日
 */
package cn.withmes.spring.cloud.eureka.client01.server;

import cn.withmes.spring.cloud.eureka.api.pojo.User;
import org.springframework.stereotype.Component;

/**
 * ClassName: FallBackDefault
 * @Description:
 * @author leegoo
 * @date 2019年07月06日
 */
//@Component
public class FallBackDefault implements   ClientSayServer{
    @Override
    public String sayinfo() {
        return "熔断了.当前方法:{sayinfo}";
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public String getMessageById(String id) {
        return "熔断了.当前方法:{getMessageById}";
    }
}
