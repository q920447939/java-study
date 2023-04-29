package cn.withmes;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Client {

    private static final int PORT = 18899;
    private static final String HOST = "localhost";
    private static final int NUM_REQUESTS = 100;

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LengthFieldPrepender(4));
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                            //pipeline.addLast(new ClientHandler());
                        }
                    });
            ChannelFuture f = b.connect(HOST, PORT).sync();
            System.out.println("Connected to server " + HOST + ":" + PORT);

            for (int i = 1; i <= NUM_REQUESTS; i++) {
                String data = "我爱学Netty" + new Random().nextInt(1000);
                ByteBuf buf = Unpooled.buffer();
                buf.writeInt(data.length());
                buf.writeBytes(data.getBytes(StandardCharsets.UTF_8));
                f.channel().writeAndFlush(buf);
                System.out.println("Sent request: " + data);
            }

            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
