/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年04月19日
 */
package com.example.nettychatclient.send;

import com.example.nettychat.common.common.bean.ChatMsg;
import com.example.nettychat.common.common.bean.User;
import com.example.nettychat.common.common.bean.msg.ProtoMsg;
import com.example.nettychatclient.protoConverter.ChatMsgConverter;
import com.example.nettychatclient.protoConverter.LoginMsgConverter;
import com.example.nettychatclient.session.ClientSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * ClassName: ChatSend
 * @Description:
 * @author leegoo
 * @date 2023年04月19日
 */
@Service
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChatSend extends BaseSendAbstract{
    public void sendChtMsg(User user, ClientSession session,String toUser,String content) {
        this.setUser(user);
        this.setSession(session);
        if (!isLogin()){
            log.warn("未登录");
            return;
        }
        if (!session.isConnected()) {
            log.warn("未连接");
            return;
        }
        ChatMsg chatMsg = new ChatMsg(user);
        long timestamp = System.currentTimeMillis();
        chatMsg.setMsgId(timestamp);
        chatMsg.setFrom(user.getUid());
        chatMsg.setTo(toUser);
        chatMsg.setTime(timestamp);
        chatMsg.setMsgType(ChatMsg.MSGTYPE.TEXT);
        chatMsg.setContent(content);
        //chatMsg.setUrl();
        //chatMsg.setProperty();
        chatMsg.setFromNick(user.getNickName());
        //chatMsg.setJson();
        ProtoMsg.Message message = ChatMsgConverter.build(chatMsg, user,session);
        log.info("发送聊天消息...channelId={}", session.getChannel().id());
        super.sendMessage(message);
    }
}
