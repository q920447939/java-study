/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月25日
 */
package com.example.nettychatserver.inbound;

import com.example.nettychat.common.ProtoInstant;
import com.example.nettychat.common.common.bean.msg.ProtoMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.SneakyThrows;

import java.util.List;

/**
 * ClassName: ProtobuffChannelHandler
 *
 * @author leegoo
 * @Description:
 * @date 2023年03月25日
 */
public class ProtobuffChannelHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object result = this.decode0(ctx, in);
        if (null != result) {
            out.add(result);
        }
    }


    @SneakyThrows
    private Object decode0(ChannelHandlerContext ctx, ByteBuf in) {
        //标记读取位置
        in.markReaderIndex();
        int size = in.readableBytes();
        if (size < 8) {
            return null;
        }
        //读取魔数
        short magic = in.readShort();
        if (magic != ProtoInstant.MAGIC_CODE) {
            //口令不对，抛一个自定义异常，关闭连接
            return null;
        }
        //读取版本号
        short version = in.readShort();
        if (version != ProtoInstant.VERSION_CODE) {
            //版本不对，抛一个自定义异常，关闭连接
            return null;
        }
        //读取长度
        int length = in.readInt();
        //长度不够判断
        if (length < 0) {
            //长度为0
            ctx.close();
            return null;
        }
        //读取到byte数组
        byte[] bytes = new byte[length];
        if (in.hasArray()) {
            in.readBytes(bytes);
        } else {
            in.readBytes(bytes);
        }
        ProtoMsg.Message parseFrom = ProtoMsg.Message.parseFrom(bytes);

        //转成protobuf 对象
        return parseFrom;
    }
}
