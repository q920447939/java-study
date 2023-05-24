package com.crazymaker.websocket.netty;

import com.crazymaker.websocket.processer.ChatProcesser;
import com.crazymaker.websocket.session.ServerSession;
import com.crazymaker.websocket.session.SessionMap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Created by 尼恩 @ 疯狂创客圈
 * <p>
 * WebSocket 帧：WebSocket 以帧的方式传输数据，每一帧代表消息的一部分。一个完整的消息可能会包含许多帧
 */
@Slf4j
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>
{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception
    {
        //增加消息的引用计数（保留消息），并将他写到 ChannelGroup 中所有已经连接的客户端

        ServerSession session = ServerSession.getSession(ctx);
        Map<String, String> result = ChatProcesser.inst().onMessage(msg.text(), session);

        if (result != null && null!=result.get("type"))
        {
            switch (result.get("type"))
            {
                case "msg":
                    SessionMap.inst().sendToOthers(result, session);
                    break;
                case "init":
                    SessionMap.inst().addSession(result, session);
                    break;
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {
        //是否握手成功，升级为 Websocket 协议
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE)
        {
            // 握手成功，移除 HttpRequestHandler，因此将不会接收到任何消息
            // 并把握手成功的 Channel 加入到 ChannelGroup 中
            ServerSession session = new ServerSession(ctx.channel());
            String echo = ChatProcesser.inst().onOpen(session);
            SessionMap.inst().sendMsg(ctx, echo);

        } else if (evt instanceof IdleStateEvent)
        {
            IdleStateEvent stateEvent = (IdleStateEvent) evt;
            if (stateEvent.state() == IdleState.READER_IDLE)
            {
                ServerSession session = ServerSession.getSession(ctx);
                SessionMap.inst().remove(session);
                session.processError(null);

            }
        } else
        {
            super.userEventTriggered(ctx, evt);
        }
    }



}
