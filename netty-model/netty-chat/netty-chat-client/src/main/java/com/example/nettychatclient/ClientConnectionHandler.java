/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年04月06日
 */
package com.example.nettychatclient;

import com.example.nettychat.common.common.codec.SimpleProtobufDecoder;
import com.example.nettychat.common.common.codec.SimpleProtobufEncoder;
import com.example.nettychatclient.pipeline.LoginResponseHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * ClassName: ClientConnectionHandler
 * @Description:
 * @author leegoo
 * @date 2023年04月06日
 */
@Service
public class ClientConnectionHandler {
    @Resource
    private LoginResponseHandler loginResponseHandler;
    private static Bootstrap  bootstrap = new Bootstrap();

    public  void  connection (GenericFutureListener<? extends Future<? super Void> > callback) {
        EventLoopGroup loopGroup = new NioEventLoopGroup();
        bootstrap.group(loopGroup);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("decoder", new SimpleProtobufDecoder());
                pipeline.addLast("encoder", new SimpleProtobufEncoder());
                pipeline.addLast(loginResponseHandler);
                //todo
            }
        });
        bootstrap.remoteAddress("127.0.0.1", 8081);
        ChannelFuture channelFuture = bootstrap.connect();
        channelFuture.addListener(callback);
    }
}
