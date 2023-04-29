/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月29日
 */
package com.example.nettychatserver.service;

import com.example.nettychat.common.ProtoInstant;
import com.example.nettychat.common.common.bean.User;
import com.example.nettychat.common.common.bean.msg.ProtoMsg;
import com.example.nettychatserver.db.DBHandle;
import com.example.nettychatserver.protoConvertor.LoginResponceConverter;
import com.example.nettychatserver.session.ServerSession;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * ClassName: LoginService
 * @Description:
 * @author leegoo
 * @date 2023年03月29日
 */
@Service("loginService")
public class LoginServiceImpl {

    @Resource
    private LoginResponceConverter loginResponceConverter;

    public boolean login (ServerSession serverSession , ProtoMsg.Message message) {
        // 取出token验证
        ProtoMsg.LoginRequest info = message.getLoginRequest();
        long seqNo = message.getSequence();

        User user = User.fromMsg(info);
        //判断用户是否登录
        //已登录 -> 直接返回
        //登录失败 -> 响应客户端登录失败  该功能不做
        //登录成功，session 中的channel和用户做绑定
        if (DBHandle.isLogin(user)) {
            //TODO 这里后续优化  DBHandle.isLogin(user) 和sesioninstan 也不同
            ProtoInstant.ResultCodeEnum resultcode =
                    ProtoInstant.ResultCodeEnum.NO_TOKEN;
            //构造登录失败的报文
            ProtoMsg.Message response =
                    loginResponceConverter.build(resultcode, seqNo, "-1");
            serverSession.writeAndFlush(response);
            return false;
        }
        serverSession.setUser(user);
        serverSession.reverseBind();
        serverSession.setLogin(true);
        //登录成功
        ProtoInstant.ResultCodeEnum resultcode =
                ProtoInstant.ResultCodeEnum.SUCCESS;
        //构造登录成功的报文
        ProtoMsg.Message response =  loginResponceConverter.build(resultcode, seqNo, serverSession.getSessionId());
        //发送登录成功的报文
        serverSession.writeAndFlush(response);
        return true;

    }
}
