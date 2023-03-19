/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月15日
 */
package cn.withmes.netty.encodeDecode.bond;

import cn.withmes.netty.encodeDecode.beyeToString.NettyServerStringPrintDecoder;
import cn.withmes.netty.v1.server.NettyEchoServerV1;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

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
            serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf byteBuf = (ByteBuf) msg;
                            log.info("[NettyEchoServerHandler] msg类型={}",byteBuf.hasArray() ? "堆内存":"直接内存");
                            int len = byteBuf.readableBytes();
                            byte [] arr = new byte[len];
                            byteBuf.getBytes(0,arr);
                            String message = new String(arr, StandardCharsets.UTF_8);
                            log.info("[NettyEchoServerHandler] 读取到的数据=[{}]", message);
                            //ChannelFuture future  = ctx.writeAndFlush("服务端回复消息=> hello! ");
                            ByteBuf sendBuff = ctx.channel().alloc().buffer();
                            sendBuff.writeBytes(("hello" + message).getBytes(StandardCharsets.UTF_8));
                            ChannelFuture future  = ctx.writeAndFlush(sendBuff);
                            future.addListener((ChannelFuture futureListener) -> {
                                log.info("[NettyEchoServerHandler] 写回后，msg.refCnt:{}",byteBuf.refCnt());
                            });
                            super.channelRead(ctx, msg);
                        }
                    });
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
