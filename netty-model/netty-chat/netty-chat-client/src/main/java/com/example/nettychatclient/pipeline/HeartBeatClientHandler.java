/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年04月26日
 */
package com.example.nettychatclient.pipeline;

import com.example.nettychat.common.common.bean.User;
import com.example.nettychat.common.common.bean.msg.ProtoMsg;
import com.example.nettychatclient.protoConverter.HeartBeatMsgConverter;
import com.example.nettychatclient.session.ClientSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: HeartBeatClientHandler
 * @Description:
 * @author leegoo
 * @date 2023年04月26日
 */
@Service
@ChannelHandler.Sharable
@Slf4j
public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("心跳包处理开始");
        ClientSession session = ClientSession.getSession(ctx);
        User user = session.getUser();
        HeartBeatMsgConverter heartBeatMsgConverter = new HeartBeatMsgConverter(user,session);
        ProtoMsg.Message message = heartBeatMsgConverter.build();
        this.heartBeat(ctx,message);
    }

    private void heartBeat(ChannelHandlerContext ctx,ProtoMsg.Message message) {
        log.debug("发送心跳包");
        ctx.executor().schedule(()->{
            if (ctx.channel().isActive()){
                log.debug("定时任务开始发送心跳包");
                ctx.writeAndFlush(message);
                heartBeat(ctx,message);
            }
        },3, TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //判断消息实例
        if (null == msg || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }
        ProtoMsg.Message message = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType messageType = message.getType();
        if (messageType.equals(ProtoMsg.HeadType.HEART_BEAT)) {
            log.debug("收到心跳包");
            return;
        }
        super.channelRead(ctx, msg);
    }
}
