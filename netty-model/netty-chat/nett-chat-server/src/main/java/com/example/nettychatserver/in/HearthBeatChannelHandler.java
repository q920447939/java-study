/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年04月27日
 */
package com.example.nettychatserver.in;

import com.example.nettychat.common.common.bean.msg.ProtoMsg;
import com.example.nettychatserver.async.AsyncCallBack;
import com.example.nettychatserver.async.CallBack;
import com.example.nettychatserver.session.ServerSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: HearthBeatChannelHandler
 * @Description:
 * @author leegoo
 * @date 2023年04月27日
 */
@Service
@Slf4j
public class HearthBeatChannelHandler extends IdleStateHandler {
    private static final int READ_IDLE_GAP = 150;

    public HearthBeatChannelHandler() {
        super(READ_IDLE_GAP, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ( !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx,msg);
            return;
        }
        ProtoMsg.Message message = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType messageType = message.getType();
        if (!messageType.equals(ProtoMsg.HeadType.HEART_BEAT)) {
            super.channelRead(ctx,msg);
            return;
        }
        log.debug("收到心跳包");
        AsyncCallBack.run(new CallBack() {
            @Override
            public boolean execute() {
                if (ctx.channel().isActive()){
                    return true;
                }
                return false;
            }

            @Override
            public boolean onSuccess() {
                ServerSession.getSession(ctx).writeAndFlush(msg);
               //ctx.writeAndFlush(msg);
                return true;
            }

            @Override
            public boolean onFail() {
                return false;
            }
        });
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        log.info(READ_IDLE_GAP + "秒内未读到数据，关闭连接");
        ServerSession.closeSession(ctx);
    }
}
