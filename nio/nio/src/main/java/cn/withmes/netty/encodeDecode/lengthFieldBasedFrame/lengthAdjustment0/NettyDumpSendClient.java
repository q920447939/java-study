/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月15日
 */
package cn.withmes.netty.encodeDecode.lengthFieldBasedFrame.lengthAdjustment0;


import cn.withmes.netty.encodeDecode.lengthFieldBasedFrame.NettyEchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;


public class NettyDumpSendClient {

    private int serverPort;
    private String serverIp;
    Bootstrap b = new Bootstrap();

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NettyDumpSendClient.class);


    public NettyDumpSendClient(String ip, int port) {
        this.serverPort = port;
        this.serverIp = ip;
    }

    public void runClient() {
        //创建reactor 线程组
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();

        try {
            //1 设置reactor 线程组
            b.group(workerLoopGroup);
            //2 设置nio类型的channel
            b.channel(NioSocketChannel.class);
            //3 设置监听端口
            b.remoteAddress(serverIp, serverPort);
            //4 设置通道的参数
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            //5 装配子通道流水线
            b.handler(new ChannelInitializer<SocketChannel>() {
                //有连接到达时会创建一个channel
                protected void initChannel(SocketChannel ch) throws Exception {
                    // pipeline管理子通道channel中的Handler
                    // 向子channel流水线添加一个handler处理器
                    ChannelPipeline pipeline = ch.pipeline();
                    //pipeline.addLast(new LengthFieldPrepender(4));
                    pipeline.addLast(NettyEchoClientHandler.INSTANCE);
                }
            });
            ChannelFuture f = b.connect();
            f.addListener((ChannelFuture futureListener) ->
            {
                if (futureListener.isSuccess()) {
                    log.info("EchoClient客户端连接成功!");

                } else {
                    log.info("EchoClient客户端连接失败!");
                }

            });

            // 阻塞,直到连接完成
            f.sync();
            Channel channel = f.channel();

            //6发送大量的文字
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                StringBuffer sb = new StringBuffer();
                //发送ByteBuf
                ByteBuf buffer = channel.alloc().buffer();
                //1-3之间的随机数
                int num = random.nextInt(5);
                num = (num == 0) ? 1 : num;
                for (int i1 = 0; i1 < num; i1++) {
                    sb.append("我爱学netty!").append("\t");
                }
                sb.append("(随机次数)=").append(num);

                //首先 写入头部  head，也就是后面的数据长度
                String str = sb.toString();
                log.info("[Clinet] 发送数据={}",str);
                byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
                buffer.writeInt( (bytes.length ) );
                buffer.writeBytes(bytes);
                /*byte [] residue = new byte[1024 - bytes.length];
                buffer.writeBytes(residue);*/
                channel.writeAndFlush(buffer);
            }
            // 7 等待通道关闭的异步任务结束
            // 服务监听通道会一直等待通道关闭的异步任务结束
            ChannelFuture closeFuture =channel.closeFuture();
            closeFuture.sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 优雅关闭EventLoopGroup，
            // 释放掉所有资源包括创建的线程
            workerLoopGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new NettyDumpSendClient("127.0.0.1", 18899).runClient();
    }
}
