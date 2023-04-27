/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月25日
 */
package com.example.nettychatserver.in;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ClassName: RequestChannelHandler
 * @Description:
 * @author leegoo
 * @date 2023年03月25日
 */
public class RequestChannelHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }
}
