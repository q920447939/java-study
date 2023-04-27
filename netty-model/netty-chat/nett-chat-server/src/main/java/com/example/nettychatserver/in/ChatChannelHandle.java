/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年04月03日
 */
package com.example.nettychatserver.in;

import cn.hutool.core.collection.CollUtil;
import com.example.nettychat.common.common.bean.msg.ProtoMsg;
import com.example.nettychatserver.session.ServerSession;
import com.example.nettychatserver.session.SessionMap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: ChatChannelHandle
 *
 * @author leegoo
 * @Description:
 * @date 2023年04月03日
 */
@Service
@ChannelHandler.Sharable
@Slf4j
public class ChatChannelHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //1.获取消息体 ，必须要是ProtoMsg.Message类型
        if (!(msg instanceof ProtoMsg.Message)) {
            log.error("当前消息类型不属于ProtoMsg.Message");
            super.channelRead(ctx, msg);
            return;
        }
        //2.判断消息体的类型是不是聊天类型
        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
        if (!pkg.getType().equals(ProtoMsg.HeadType.MESSAGE_REQUEST)) {
            //log.warn("当前消息type 不属于 ProtoMsg.HeadType.MESSAGE_REQUEST");
            super.channelRead(ctx, msg);
            return;
        }
        //3.获取到当前channel的会话信息
        ServerSession serverSession = ctx.channel().attr(ServerSession.SESSION_KEY).get();
        if (null == serverSession) {
            //4.未获取到那么处理未登陆事件
            log.warn("serverSession is null");
            super.channelRead(ctx, msg);
            return;
        }
        //5.从消息体解析出 to的id （to 可能有多个，因为多个终端设备）
        ProtoMsg.MessageRequest msgReq = pkg.getMessageRequest();
        String toUId = msgReq.getTo();
        //6.在全局会话管理中，通过to的id进行查找，看人在不在线 ->不在线进行提示
        List<ServerSession> toServerSessionList = SessionMap.inst().getSessionsBy(toUId);
        if (CollUtil.isEmpty(toServerSessionList)) {
            log.warn("未获取到接收人，接收人不在线");
            super.channelRead(ctx, msg);
            return;
        }
        //7.如果在线 ，先判断这个channel能不能发送 -> 能发送 -> 那么把消息体中需要发送的消息发送给to
        log.info("消息发送人为：{} ,消息接收人为:{},发送内容为:{}",
                serverSession.getUser().getUid(), toServerSessionList.stream().map(k -> k.getUser().getUid()).collect(Collectors.toList()), msgReq.getContent());
        if (!ctx.channel().isWritable()) {
            log.warn("该通道暂不可写");
            super.channelRead(ctx, msg);
            return;
        }
        for (ServerSession session : toServerSessionList) {
            session.writeAndFlush(pkg);
        }
        super.channelRead(ctx, msg);
    }
}
