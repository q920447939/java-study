/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月14日
 */
package cn.withme.netty.netty4;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * ClassName: NettyClient
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月14日
 */
public class NettyClient {

    static class ClientHandler extends SimpleChannelInboundHandler {
        /**
         * 当通道被调用,执行该方法
         */
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("服务端说 : " + msg.toString());
        }

    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("客户端已经启动....");
        // 创建负责接收客户端连接
        NioEventLoopGroup pGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(pGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel sc) throws Exception {
                ByteBuf buf = Unpooled.copiedBuffer("_bb".getBytes());
                sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
                sc.pipeline().addLast(new StringDecoder());
                sc.pipeline().addLast(new ClientHandler());

            }
        });
        ChannelFuture cf = b.connect("127.0.0.1", 5876).sync();
        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("这是一条消息_bb".getBytes()));
        // 等待客户端端口号关闭
        cf.channel().closeFuture().sync();
        pGroup.shutdownGracefully();


    }
}