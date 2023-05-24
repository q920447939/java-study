package com.crazymaker.websocket.netty;

import com.crazymaker.websocket.session.SessionMap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;


/**
 * Netty 服务
 * Created by 尼恩 @ 疯狂创客圈
 */
public class WebSocketServer
{

    /**
     * 单例
     */
    private static WebSocketServer singleInstance = new WebSocketServer();

    public static WebSocketServer inst()
    {
        return singleInstance;
    }


    private final EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;


    /**
     * 停止即时通讯服务器
     */
    public void stop()
    {
        if (channel != null)
        {
            channel.close();
        }
        SessionMap.inst().shutdownGracefully();
        group.shutdownGracefully();
    }

    /**
     * 启动即时通讯服务器
     */
    public void start()
    {
        final WebSocketServer webSocketServer = new WebSocketServer();


        ChannelFuture channelFuture = null;
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChatServerInitializer());
        InetSocketAddress address = new InetSocketAddress(9999);
        channelFuture = bootstrap.bind(address);
        channelFuture.syncUninterruptibly();

        channel = channelFuture.channel();
        // 返回与当前Java应用程序关联的运行时对象
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                webSocketServer.stop();
            }
        });
        channelFuture.channel().closeFuture().syncUninterruptibly();
    }

    /**
     * 内部类
     */
    class ChatServerInitializer extends ChannelInitializer<Channel>
    {
        private static final int READ_IDLE_TIME_OUT = 60; // 读超时  s
        private static final int WRITE_IDLE_TIME_OUT = 0;// 写超时
        private static final int ALL_IDLE_TIME_OUT = 0; // 所有超时


        @Override
        protected void initChannel(Channel ch) throws Exception
        {
            ChannelPipeline pipeline = ch.pipeline();
            // Netty自己的http解码器和编码器，报文级别 HTTP请求的解码和编码
            pipeline.addLast(new HttpServerCodec());
            // ChunkedWriteHandler 是用于大数据的分区传输
            // 主要用于处理大数据流，比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的;
            // 增加之后就不用考虑这个问题了
            pipeline.addLast(new ChunkedWriteHandler());
            // HttpObjectAggregator 是完全的解析Http消息体请求用的
            // 把多个消息转换为一个单一的完全FullHttpRequest或是FullHttpResponse，
            // 原因是HTTP解码器会在每个HTTP消息中生成多个消息对象HttpRequest/HttpResponse,HttpContent,LastHttpContent
            pipeline.addLast(new HttpObjectAggregator(64 * 1024));
            // WebSocket数据压缩
            pipeline.addLast(new WebSocketServerCompressionHandler());
            // WebSocketServerProtocolHandler是配置websocket的监听地址/协议包长度限制
            pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true, 10 * 1024));

            //当连接在60秒内没有接收到消息时，就会触发一个 IdleStateEvent 事件，
            // 此事件被 HeartbeatHandler 的 userEventTriggered 方法处理到
            pipeline.addLast(new IdleStateHandler(READ_IDLE_TIME_OUT, WRITE_IDLE_TIME_OUT, ALL_IDLE_TIME_OUT, TimeUnit.SECONDS));

            //WebSocketServerHandler、TextWebSocketFrameHandler 是自定义逻辑处理器，
            pipeline.addLast(new TextWebSocketFrameHandler());

        }
    }
}
