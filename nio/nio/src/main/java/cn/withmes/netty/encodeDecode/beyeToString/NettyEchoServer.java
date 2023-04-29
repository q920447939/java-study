/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月15日
 */
package cn.withmes.netty.encodeDecode.beyeToString;

import cn.withmes.netty.v1.server.NettyEchoServerV1;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.LoggerFactory;

/**
 * ClassName: NettyEchoServer
 *
 * @author leegoo
 * @Description:
 * 1.创建serverboot
 * 2.创建parent,children
 * 3.把serverboost绑定parent,children ，绑定IP , 设置channel类型
 * 4.设置参数  keepalieve
 * 5.设置children初始化 -> 设置pipline
 * 6.获取channel
 * 7.注册是否channel完成事件
 * 8.channel关闭事件
 * 9.parent,children 优雅关闭
 * @date 2023年03月15日
 */
public class NettyEchoServer {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NettyEchoServerV1.class);

    public void startServer() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup parent = new NioEventLoopGroup(1);
        EventLoopGroup children = new NioEventLoopGroup();
        serverBootstrap.group(parent, children);
        try {
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress(18899);
            serverBootstrap.option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);
            serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.childHandler(new ChannelInitializer<ServerChannel>() {
                @Override
                protected void initChannel(ServerChannel ch) throws Exception {
                    ch.pipeline().addLast(new NettyServerStringDecoder());
                    ch.pipeline().addLast(new NettyServerStringPrintDecoder());
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    log.info("【服务端】连接成功");
                }
            });
            while (!channelFuture.isSuccess()) {
                channelFuture.awaitUninterruptibly();
            }
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception ex) {
            log.error("【服务端】异常",ex);
        }finally {
            parent.shutdownGracefully();
            children.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyEchoServer().startServer();
    }
}
