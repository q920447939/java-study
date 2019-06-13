/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月13日
 */
package cn.withme.netty;


import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName: Test01
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月13日
 */
public class NettyServer {

    static class ServerHandler extends SimpleChannelHandler {


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
            ctx.getChannel().write("你好啊!");
            System.out.println("接收到客户端的数据:" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        //1.创建服务对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //2.创建两个线程池,第一个 监听端口号 nio监听
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService work = Executors.newCachedThreadPool();

        //
        serverBootstrap.setFactory(new NioServerSocketChannelFactory(boss, work));

        //3.将线程池放入工程
        serverBootstrap.setPipelineFactory(() -> {
            //4.设置编码格式
            ChannelPipeline pipeline = Channels.pipeline();
            pipeline.addLast("decoder", new StringDecoder());
            pipeline.addLast("encoder", new StringEncoder());
            pipeline.addLast("serverHanlder", new ServerHandler());
            return pipeline;
        });

        serverBootstrap.bind(new InetSocketAddress(9999));
    }
}
