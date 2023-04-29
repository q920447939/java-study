/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月13日
 */
package cn.withmes.netty.v1.client;

import cn.withmes.netty.v1.server.NettyEchoServerV1;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * ClassName: NettyEchoClientV1
 * @Description:
 * @author leegoo
 * @date 2023年03月13日
 */
public class NettyEchoClientV1 {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NettyEchoClientV1.class);

    public void start(){
        /*
        1.创建boostrap
        2.创建workEventLoop
        3.把workEventLoop绑定到boostrap上
        4.绑定远程ip和端口
        5.设置boostrap参数
        6.设置管道类型
        7.设置children初始化事件 -> pipline.addLast  ->@Share
        8.while循环进行连接判断， 没有连接的话使用awaitUnitnter..
        9.连接成功。
        10.创建Scan,while循环判断HasNext
        11.取出next
        12.分配内存 是否可以使用内存增加 减少对象创建？
        13.注册回调事件
        14.转成字符串 utf8 -> 然后writeAndFlush
        15.优雅关闭
         */
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup workEventLoopGroup = new NioEventLoopGroup();
        try {
            bootstrap.group(workEventLoopGroup);
            bootstrap.remoteAddress("127.0.0.1",8081);
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(NettyEchoClientHandler.INSTANCE);
                }
            });
            ChannelFuture channelFuture = bootstrap.connect();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    log.info("[NettyEchoClientV1] 连接服务端成功");
                }
            });
            while (!channelFuture.isSuccess()) {
                channelFuture.awaitUninterruptibly();
            }
            Channel channel = channelFuture.channel();
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String next = scanner.next();
                ByteBuf buffer = channel.alloc().buffer();
                buffer.writeBytes(next.getBytes(StandardCharsets.UTF_8));
                ChannelFuture writeAndFlush = channel.writeAndFlush(buffer);
                writeAndFlush.addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("[NettyEchoClientV1] 发送数据【{}】成功",next);
                    }else {
                        log.info("[NettyEchoClientV1] 发送数据失败");
                    }
                });
            }
        } catch (Exception ex) {
            log.error("[NettyEchoClientV1] 处理异常",ex);
        }finally {
            workEventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyEchoClientV1().start();
    }
}
