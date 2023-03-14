/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月13日
 */
package cn.withmes.netty.v1.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * ClassName: NettyEchoServerHandler
 * @Description:
 * 1.继承netty input adapt ，并重写channelRead
 * 2.强转为ByteBuf
 * 3.获取长度，读取到数据，然后转成字符串
 * 4.写回数据 writeAndFlush
 *
 * @author leegoo
 * @date 2023年03月13日
 */
@ChannelHandler.Sharable
public class NettyEchoServerHandler extends ChannelInboundHandlerAdapter {
    public static final NettyEchoServerHandler INSTANCE = new NettyEchoServerHandler();

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NettyEchoServerHandler.class);


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
}
