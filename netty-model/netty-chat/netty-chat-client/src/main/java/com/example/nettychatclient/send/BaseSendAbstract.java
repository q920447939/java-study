/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年04月19日
 */
package com.example.nettychatclient.send;

import com.example.nettychat.common.common.bean.User;
import com.example.nettychat.common.common.bean.msg.ProtoMsg;
import com.example.nettychatclient.session.ClientSession;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName: BaseSendAbstract
 * @Description:
 * @author leegoo
 * @date 2023年04月19日
 */
@Slf4j
@Data
public abstract class BaseSendAbstract {
    private ClientSession session;
    private User user;

    public boolean isConnection() {
        return this.session.isConnected();
    }

    public boolean isLogin(){
        return this.session.isLogin();
    }

    public void sendMessage (ProtoMsg.Message message) {
        if (!this.isConnection()) {
            log.error("未连接服务器！");
            return;
        }
        ChannelFuture future = this.session.getChannel().writeAndFlush(message);
        future.addListener((ChannelFutureListener) future1 -> {
            if (future1.isSuccess()){
                onSuccess();
            }else {
                onFail();
            }
        });
    }
    protected void onSuccess () {
        log.info("发送消息成功！");
    }

    protected void onFail () {
        log.error("发送消息失败！");
    }
}
