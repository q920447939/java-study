package cn.withmes;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.List;

public class NettyServer {

    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new FixedLengthFrameDecoder(1024));
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                            pipeline.addLast(new LengthFieldPrepender(4));
                            pipeline.addLast(new MessageToByteEncoder<String>() {
                                @Override
                                protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
                                    byte[] bytes = msg.getBytes(CharsetUtil.UTF_8);
                                    out.writeInt(bytes.length);
                                    out.writeBytes(bytes);
                                }
                            });
                            pipeline.addLast(new MessageToMessageDecoder<ByteBuf>() {
                                @Override
                                protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
                                    String str = msg.toString(CharsetUtil.UTF_8);
                                    out.add(str);
                                }
                            });
                            pipeline.addLast(new ServerHandler());
                        }
                    });

            bootstrap.bind(port).sync().channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    private static class ServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String request = (String) msg;
            System.out.println("Received request: " + request);
            String response = "Hello, " + request + "!";
            ByteBuf encoded = Unpooled.copiedBuffer(response, CharsetUtil.UTF_8);
            ctx.write(encoded);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        NettyServer server = new NettyServer(18899);
        server.start();
    }
}
