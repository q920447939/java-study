/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月25日
 */
package com.example.nettychatserver.out;

import com.example.nettychat.common.ProtoInstant;
import com.example.nettychat.common.common.bean.msg.ProtoMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName: ProtobuffChannelOutBoundHandler
 *
 * @author leegoo
 * @Description:
 * @date 2023年03月25日
 */
@Slf4j
public class ProtobuffChannelOutBoundHandler extends MessageToByteEncoder<ProtoMsg.Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          ProtoMsg.Message msg, ByteBuf out)
            throws Exception {
        encode0(ctx, msg, out);
    }

    public static void encode0(ChannelHandlerContext ctx,
                               ProtoMsg.Message msg, ByteBuf out) {
        out.writeShort(ProtoInstant.MAGIC_CODE);
        out.writeShort(ProtoInstant.VERSION_CODE);

        byte[] bytes = msg.toByteArray();// 将 ProtoMsg.Message 对象转换为byte

        // 加密消息体
        /*ThreeDES des = channel.channel().attr(Constants.ENCRYPT).get();
        byte[] encryptByte = des.encrypt(bytes);*/
        int length = bytes.length;// 读取消息的长度

        log.info("encoder length=" + length);

        // 先将消息长度写入，也就是消息头
        out.writeInt(length);
        // 消息体中包含我们要发送的数据
        out.writeBytes(bytes);

        log.debug("send "
                + "[remote ip:" + ctx.channel().remoteAddress()
                + "][total length:" + length
                + "][bare length:" + msg.getSerializedSize() + "]");


    }

}

