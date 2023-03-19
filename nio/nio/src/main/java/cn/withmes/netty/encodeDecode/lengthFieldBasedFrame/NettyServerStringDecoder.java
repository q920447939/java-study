/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月15日
 */
package cn.withmes.netty.encodeDecode.lengthFieldBasedFrame;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * ClassName: NettyServerIntDecoder
 *
 * @author leegoo
 * @Description:
 * @date 2023年03月15日
 */
public class NettyServerStringDecoder extends ReplayingDecoder<NettyServerStringDecoder.State> {
    public static enum State {
        PHASE_1,
        PHASE_2,
    }

    private int len;

    public NettyServerStringDecoder() {
        super(State.PHASE_1);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
            case PHASE_1:
                len = in.readInt();
                checkpoint(State.PHASE_2);
                break;
            case PHASE_2:
                byte[] arr = new byte[len];
                in.readBytes(arr, 0, len);
                out.add(new String(arr, StandardCharsets.UTF_8));
                checkpoint(State.PHASE_1);
                break;
            default:
                break;
        }
    }
}
