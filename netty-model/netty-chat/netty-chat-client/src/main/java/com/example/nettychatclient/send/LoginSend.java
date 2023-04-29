/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年04月19日
 */
package com.example.nettychatclient.send;

import com.example.nettychat.common.common.bean.User;
import com.example.nettychat.common.common.bean.msg.ProtoMsg;
import com.example.nettychatclient.protoConverter.LoginMsgConverter;
import com.example.nettychatclient.session.ClientSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * ClassName: LoginSend
 * @Description:
 * @author leegoo
 * @date 2023年04月19日
 */
@Service
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoginSend extends BaseSendAbstract{
    public void sendLoginMsg(User user, ClientSession session) {
        this.setUser(user);
        this.setSession(session);
        ProtoMsg.Message message =
                LoginMsgConverter.build(getUser(), getSession());
        log.info("发送登录消息...channelId={}", session.getChannel().id());
        super.sendMessage(message);
    }
}
