/**
 * Created by 尼恩 at 疯狂创客圈
 */

package com.example.nettychatclient.protoConverter;


import com.example.nettychat.common.common.bean.User;
import com.example.nettychat.common.common.bean.msg.ProtoMsg;
import com.example.nettychatclient.session.ClientSession;

/**
 * 登陆消息 Converter
 */
public class LoginMsgConverter extends BaseConverter {
    private final User user;

    public LoginMsgConverter(User user, ClientSession session) {
        super(ProtoMsg.HeadType.LOGIN_REQUEST, session);
        this.user = user;
    }

    public ProtoMsg.Message build() {

        ProtoMsg.Message.Builder outerBuilder = getOuterBuilder(-1);


           ProtoMsg.LoginRequest.Builder lb =
                ProtoMsg.LoginRequest.newBuilder()
                        .setDeviceId(user.getDevId())
                        .setPlatform(user.getPlatform().ordinal())
                        .setToken(user.getToken())
                        .setUid(user.getUid());

        ProtoMsg.Message requestMsg = outerBuilder.setLoginRequest(lb).build();

        return requestMsg;
    }


    public static ProtoMsg.Message build(  User user, ClientSession session) {
        LoginMsgConverter converter =  new LoginMsgConverter(user, session);
        return converter.build();

    }


}


