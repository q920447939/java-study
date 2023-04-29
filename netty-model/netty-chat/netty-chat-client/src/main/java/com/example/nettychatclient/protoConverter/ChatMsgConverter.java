/**
 * Created by 尼恩 at 疯狂创客圈
 */

package com.example.nettychatclient.protoConverter;


import com.example.nettychat.common.common.bean.ChatMsg;
import com.example.nettychat.common.common.bean.User;
import com.example.nettychat.common.common.bean.msg.ProtoMsg;
import com.example.nettychatclient.session.ClientSession;

/**
 * 聊天消息 Converter
 */

public class ChatMsgConverter extends BaseConverter {


    private ChatMsg chatMsg;
    private User user;


    private ChatMsgConverter(ClientSession session) {
        super(ProtoMsg.HeadType.MESSAGE_REQUEST, session);


    }


    public ProtoMsg.Message build(ChatMsg chatMsg, User user) {

        this.chatMsg = chatMsg;
        this.user = user;

        ProtoMsg.Message.Builder outerBuilder = getOuterBuilder(-1);


        ProtoMsg.MessageRequest.Builder cb =
                ProtoMsg.MessageRequest.newBuilder();
        //填充字段
        this.chatMsg.fillMsg(cb);
        ProtoMsg.Message requestMsg = outerBuilder.setMessageRequest(cb).build();

        return requestMsg;
    }

    public static ProtoMsg.Message build(
            ChatMsg chatMsg,
            User user,
            ClientSession session) {

        ChatMsgConverter chatMsgConverter = new ChatMsgConverter(session);


        return chatMsgConverter.build(chatMsg, user);

    }


}
