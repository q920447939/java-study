/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年04月06日
 */
package com.example.nettychatclient.event;

import cn.hutool.core.util.StrUtil;
import com.example.nettychat.common.common.bean.User;
import com.example.nettychatclient.event.enums.EvEntEnums;
import com.example.nettychatclient.send.LoginSend;
import com.example.nettychatclient.session.ClientSession;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * ClassName: LoginEvent
 *
 * @author leegoo
 * @Description:
 * @date 2023年04月06日
 */
@Service
@Slf4j
public class LoginEvent implements Event {
    @Resource
    private LoginSend loginSend;

    private final Scanner scanner = new Scanner(System.in);
    @Override
    public EvEntEnums getType() {
        return EvEntEnums.LOGIN;
    }

    @Override
    public void operate(String input, ClientSession session) {
        log.info("请输入用户名密码（例如a:b）");
        String userName = null;
        String password = null;
        while (scanner.hasNext()){
            String next = scanner.next();
            String[] split = next.split(":");
            if (split.length != 2){
                log.info("用户名密码格式不正确，请重新输入");
                continue;
            }
            userName = split[0];
            password = split[1];
            if (StrUtil.isBlank(userName) || StrUtil.isBlank(password)){
                log.info("用户名或密码不能为空，请重新输入");
                continue;
            }
            log.info("用户名：{}，密码：{}",userName,password);
            break;
        }
        User user = new User();
        user.setUid(userName);
        user.setToken(password);
        user.setDevId("1111");
        session.setUser(user);
        loginSend.sendLoginMsg(user,session);
    }
}
