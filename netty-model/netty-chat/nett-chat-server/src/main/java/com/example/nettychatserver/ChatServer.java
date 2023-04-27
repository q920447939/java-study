/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月25日
 */
package com.example.nettychatserver;

import com.example.nettychatserver.in.LoginRequestChannelHandler;
import com.example.nettychatserver.in.ProtobuffChannelInHandler;
import com.example.nettychatserver.in.RequestChannelHandler;
import com.example.nettychatserver.out.ProtobuffChannelOutBoundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.Resource;
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

    @Resource
    private LoginRequestChannelHandler loginRequestChannelHandler;

    public void runServe() {
        NioEventLoopGroup parent = new NioEventLoopGroup();
        NioEventLoopGroup children = new NioEventLoopGroup();
        serverBootstrap.group(parent,children);
        try {
            serverBootstrap.option(ChannelOption.ALLOCATOR,   PooledByteBufAllocator.DEFAULT);
            serverBootstrap.childOption(ChannelOption.ALLOCATOR,   PooledByteBufAllocator.DEFAULT);
            serverBootstrap.localAddress(8081);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new ProtobuffChannelInHandler());
                    pipeline.addLast("login",loginRequestChannelHandler);
                    pipeline.addLast(new ProtobuffChannelOutBoundHandler());
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
