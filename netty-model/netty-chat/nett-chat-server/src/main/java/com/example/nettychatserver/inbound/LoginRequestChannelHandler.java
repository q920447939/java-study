/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月29日
 */
package com.example.nettychatserver.inbound;

import com.example.nettychat.common.common.bean.msg.ProtoMsg;
import com.example.nettychatserver.async.AsyncCallBack;
import com.example.nettychatserver.async.CallBack;
import com.example.nettychatserver.service.LoginServiceImpl;
import com.example.nettychatserver.session.ServerSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ClassName: LoginRequestChannelHandler
 * @Description:
 * @author leegoo
 * @date 2023年03月29日
 */
@Service
@Slf4j
public class LoginRequestChannelHandler extends ChannelInboundHandlerAdapter {

    @Resource(name = "loginService")
    private LoginServiceImpl loginService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("收到一个新连接，但是还没有登录 {}",ctx.channel().id());
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ( !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx,msg);
            return;
        }
        ProtoMsg.Message message = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType messageType = message.getType();
        if (!messageType.equals(ProtoMsg.HeadType.LOGIN_REQUEST)) {
            super.channelRead(ctx,msg);
        }
        ServerSession serverSession = new ServerSession(ctx.channel());
        AsyncCallBack.run(new CallBack() {
            @Override
            public boolean execute() {
                return loginService.login(serverSession, message);
            }

            @Override
            public boolean onSuccess() {
                return false;
            }

            @Override
            public boolean onFail() {
                return false;
            }
        });

        super.channelRead(ctx, msg);
    }
}
