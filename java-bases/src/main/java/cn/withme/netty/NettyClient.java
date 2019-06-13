/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月13日
 */
package cn.withme.netty;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName: NettyClient
 * @Description:
 * @author leegoo
 * @date 2019年06月13日
 */
public class NettyClient {
    static class ClientHandler extends SimpleChannelHandler {


        //管道关闭
        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            System.out.println("管道关闭");
            super.channelClosed(ctx, e);
        }

        //建立连接之后,管道关闭
        @Override
        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            System.out.println("建立连接之后,管道关闭");
            super.channelDisconnected(ctx, e);
        }

        //接受出现异常
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            System.out.println("接受出现异常");
            super.exceptionCaught(ctx, e);
        }


        //接受客户端数据
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            super.messageReceived(ctx, e);
            System.out.println("服务端发送过来的信息:" + e.getMessage());
        }
    }




    public static void main(String[] args) {
        //1.创建服务对象
        ClientBootstrap clientBootstrap = new ClientBootstrap();
        //2.创建两个线程池,第一个 监听端口号 nio监听
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService work = Executors.newCachedThreadPool();

        //
        clientBootstrap.setFactory(new NioClientSocketChannelFactory(boss, work));

        //3.将线程池放入工程
        clientBootstrap.setPipelineFactory(() -> {
            //4.设置编码格式
            ChannelPipeline pipeline = Channels.pipeline();
            pipeline.addLast("decoder", new StringDecoder());
            pipeline.addLast("encoder", new StringEncoder());
            pipeline.addLast("clientHanlder", new ClientHandler());
            return pipeline;
        });

        ChannelFuture connect = clientBootstrap.connect(new InetSocketAddress("127.0.0.1", 9999));
        Channel channel = connect.getChannel();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("请输入需要往服务器发送的内容:");
            channel.write(scanner.next());

        }
    }

}
