/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月25日
 */
package com.example.nettychatserver;

import com.example.nettychatserver.inbound.ProtobuffChannelHandler;
import com.example.nettychatserver.inbound.RequestChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * ClassName: ChatServer
 * @Description:
 * @author leegoo
 * @date 2023年03月25日
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChatServer {
    private ServerBootstrap serverBootstrap = new ServerBootstrap();

    public void runServe() {
        NioEventLoopGroup parent = new NioEventLoopGroup();
        NioEventLoopGroup children = new NioEventLoopGroup();
        serverBootstrap.group(parent,children);
        try {
            serverBootstrap.option(ChannelOption.ALLOCATOR,   PooledByteBufAllocator.DEFAULT);
            serverBootstrap.childOption(ChannelOption.ALLOCATOR,   PooledByteBufAllocator.DEFAULT);
            serverBootstrap.localAddress(18899);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new ProtobuffChannelHandler());
                    pipeline.addLast(new RequestChannelHandler());
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            ChannelFuture future = channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            parent.shutdownGracefully();
            children.shutdownGracefully();
        }
    }
}
