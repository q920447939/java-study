/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月29日
 */
package com.example.nettychatserver.service;

import com.example.nettychat.common.common.bean.User;
import com.example.nettychat.common.common.bean.msg.ProtoMsg;
import com.example.nettychatserver.session.ServerSession;
import org.springframework.stereotype.Service;

/**
 * ClassName: LoginService
 * @Description:
 * @author leegoo
 * @date 2023年03月29日
 */
@Service("loginService")
public class LoginServiceImpl {


    public boolean login (ServerSession serverSession , ProtoMsg.Message message) {
        // 取出token验证
        ProtoMsg.LoginRequest info = message.getLoginRequest();
        long seqNo = message.getSequence();

        User user = User.fromMsg(info);
        return false;

    }
}
