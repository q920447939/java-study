/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月15日
 */
package cn.withmes.netty.encodeDecode.byteToInt;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.LoggerFactory;

/**
 * ClassName: NettyServerIntDecoder
 *
 * @author leegoo
 * @Description:
 * @date 2023年03月15日
 */
public class NettyServerIntPrintDecoder extends ChannelInboundHandlerAdapter {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NettyServerIntPrintDecoder.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        int num = (int) msg;
        log.info("[NettyServerIntPrintDecoder] -> 接收到的数据:{}",num);
    }
}
