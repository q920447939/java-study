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
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ClassName: LoginResponseHandler
 * @Description:
 * @author leegoo
 * @date 2023年04月26日
 */
@Service
@ChannelHandler.Sharable
@Slf4j
public class LoginResponseHandler extends ChannelInboundHandlerAdapter {
    @Resource
    private ChatResponseHandler chatMsgHandler;
    @Resource
    private HeartBeatClientHandler heartBeatClientHandler;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //判断消息实例
        if (null == msg || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }
        ProtoMsg.Message message = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType messageType = message.getType();
        if (!messageType.equals(ProtoMsg.HeadType.LOGIN_RESPONSE)) {
            log.info("消息类型不是登录响应");
            super.channelRead(ctx,msg);
            return;
        }
        //判断返回是否成功
        ProtoMsg.LoginResponse info = message.getLoginResponse();

        ProtoInstant.ResultCodeEnum result =
                ProtoInstant.ResultCodeEnum.values()[info.getCode()];
        if (!result.equals(ProtoInstant.ResultCodeEnum.SUCCESS)) {
            log.info("登录失败");
            super.channelRead(ctx,msg);
            return;
        }else {
            log.info("登录成功");
            //登录成功
            ClientSession.loginSuccess(ctx, message);
            ChannelPipeline p = ctx.pipeline();
            //移除登录响应处理器
            p.remove(this);
            p.addAfter("encoder", "chat",chatMsgHandler);
            //在编码器后面，动态插入心跳处理器
            p.addAfter("encoder", "heartbeat", heartBeatClientHandler);
            heartBeatClientHandler.channelActive(ctx);
        }


    }
}
