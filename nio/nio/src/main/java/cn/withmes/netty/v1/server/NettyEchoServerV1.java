/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月13日
 */
package cn.withmes.netty.v1.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.LoggerFactory;

public class NettyEchoServerV1 {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NettyEchoServerV1.class);

    public void startServer() {
          /*
        1.创建serverboot
        2.创建parent,children
        3.把serverboost绑定parent,children ，绑定IP , 设置channel类型
        4.设置参数  keepalieve
        5.设置children初始化 -> 设置pipline
        6.获取channel
        7.注册是否channel完成事件
        8.channel关闭事件
        9.parent,children 优雅关闭
         */
        EventLoopGroup parent = null, children = null;
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            parent = new NioEventLoopGroup();
            children = new NioEventLoopGroup();
            serverBootstrap.group(parent, children);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress(18899);
            serverBootstrap.option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);
            serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(NettyEchoServerHandler.INSTANCE);
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("服务管道建立成功！");
                    log.info("服务管道建立成功！,绑定端口={}", channelFuture.channel().localAddress());
                }
            });
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception ex) {
            log.info("netty 服务端异常", ex);
        } finally {
            if (null != parent) {
                parent.shutdownGracefully();
            }
            if (null != children) {
                children.shutdownGracefully();
            }
        }
    }

    public static void main(String[] args) {
        new NettyEchoServerV1().startServer();
    }
}
