/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年04月28日
 */
package cn.withmes.nettyechohttp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

/**
 * ClassName: NettyEchoServer
 * @Description:
 * @author leegoo
 * @date 2023年04月28日
 */

@Service
public class NettyEchoServer {
    @Resource
    private NettyEchoServerHandler nettyEchoServerHandler;

    @SneakyThrows
    public void startServer() {
        //创建bootstrap,并且group
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup parent = new NioEventLoopGroup(1);
        NioEventLoopGroup children = new NioEventLoopGroup();
        serverBootstrap.group(parent,children);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new HttpRequestDecoder());
                pipeline.addLast(new HttpResponseEncoder());
                pipeline.addLast(new HttpObjectAggregator(1024*1024));
                pipeline.addLast(nettyEchoServerHandler);
            }
        });
        serverBootstrap.bind(8081).sync().channel().closeFuture().sync();

    }
}
