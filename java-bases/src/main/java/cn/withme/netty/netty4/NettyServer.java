/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月14日
 */
package cn.withme.netty.netty4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * ClassName: NettyServer
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月14日
 */
public class NettyServer {

    static class ServerHandler extends SimpleChannelInboundHandler {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("客户端说 : " + msg.toString());
            //返回客户端消息 - 我已经接收到了你的消息
            ctx.writeAndFlush("你好,傻叉");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("RemoteAddress : " + ctx.channel().remoteAddress() + " active !");
            ctx.writeAndFlush("连接成功！");
            super.channelActive(ctx);
        }


    }

    public static void main(String[] args) throws Exception {
        System.out.println("服务端已经启动了....");
        //1,创建两个线程, 一个负责接收客户端的连接,一个负责进行传输数据
        NioEventLoopGroup pGroup = new NioEventLoopGroup();
        NioEventLoopGroup cGroup = new NioEventLoopGroup();
        //2,创建服务端辅助类
        ServerBootstrap b = new ServerBootstrap();
        b.group(pGroup, cGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
                // 3.设置缓冲区与发送区大小
                .option(ChannelOption.SO_SNDBUF, 32 * 1024).option(ChannelOption.SO_RCVBUF, 32 * 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        ByteBuf buf = Unpooled.copiedBuffer("_bb".getBytes());
                        sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
                        sc.pipeline().addLast(new StringDecoder());
                        sc.pipeline().addLast(new ServerHandler());
                    }
                });
        ChannelFuture cf = b.bind(5876).sync();
        cf.channel().closeFuture().sync();
        pGroup.shutdownGracefully();
        cGroup.shutdownGracefully();

    }
}
