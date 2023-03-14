/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月13日
 */
package cn.withmes.netty.v1.client;

import cn.withmes.netty.v1.server.NettyEchoServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * ClassName: NettyEchoClientHandler
 * @Description:
 * @author leegoo
 * @date 2023年03月13日
 */
@ChannelHandler.Sharable
public class NettyEchoClientHandler extends ChannelInboundHandlerAdapter {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NettyEchoClientHandler.class);

    public static final NettyEchoClientHandler INSTANCE = new NettyEchoClientHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        int len = byteBuf.readableBytes();
        byte [] arr = new byte[len];
        byteBuf.getBytes(0,arr);
        log.info("[NettyEchoClientHandler] 读取到的数据={}",new String(arr, StandardCharsets.UTF_8));
        byteBuf.release();
        super.channelRead(ctx, msg);
    }
}
