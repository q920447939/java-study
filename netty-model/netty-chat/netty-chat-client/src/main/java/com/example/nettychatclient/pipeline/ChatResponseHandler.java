/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年04月26日
 */
package com.example.nettychatclient.pipeline;

import com.example.nettychat.common.ProtoInstant;
import com.example.nettychat.common.common.bean.msg.ProtoMsg;
import com.example.nettychatclient.session.ClientSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ClassName: ChatResponseHandler
 * @Description:
 * @author leegoo
 * @date 2023年04月26日
 */
@Service
@ChannelHandler.Sharable
@Slf4j
public class ChatResponseHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //判断消息实例
        if (null == msg || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }
        ProtoMsg.Message message = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType messageType = message.getType();
        if (!messageType.equals(ProtoMsg.HeadType.MESSAGE_REQUEST)) {
            //log.info("消息类型不是聊天响应");
            super.channelRead(ctx,msg);
            return;
        }
        ProtoMsg.MessageRequest messageRequest = message.getMessageRequest();
        log.info("接收到聊天消息：发送人={},内容={}",messageRequest.getFrom(),messageRequest.getContent());
        //messageResponse.getContent();
    }
}
