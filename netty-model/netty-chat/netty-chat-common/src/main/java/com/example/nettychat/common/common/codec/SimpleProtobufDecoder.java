package com.example.nettychat.common.common.codec;

import com.example.nettychat.common.ProtoInstant;
import com.example.nettychat.common.common.bean.msg.ProtoMsg;
import com.example.nettychat.common.common.exception.InvalidFrameException;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
/**
 * create by 尼恩 @ 疯狂创客圈
 **/


/**
 * create by 尼恩 @ 疯狂创客圈
 * <p>
 * 解码器
 */


public class SimpleProtobufDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in,
                          List<Object> out) throws Exception {

        Object outmsg = decode0(ctx, in);
        if (outmsg != null) {
            // 获取业务消息
            out.add(outmsg);
        }

    }

    public static Object decode0(ChannelHandlerContext ctx,
                                 ByteBuf in) throws InvalidFrameException, InvalidProtocolBufferException {
        // 标记一下当前的readIndex的位置
        in.markReaderIndex();
        // 判断包头长度
        if (in.readableBytes() < 8) {// 不够包头
            return null;
        }
        //读取魔数
        short magic = in.readShort();
        if (magic != ProtoInstant.MAGIC_CODE) {
            String error = "客户端口令不对:" + ctx.channel().remoteAddress();
            //异常连接，直接报错，关闭连接
            throw new InvalidFrameException(error);
        }
        //读取版本
        short version = in.readShort();
        if (version != ProtoInstant.VERSION_CODE) {
            String error = "协议的版本不对:" + ctx.channel().remoteAddress();
            //异常连接，直接报错，关闭连接
            throw new InvalidFrameException(error);
        }
        // 读取传送过来的消息的长度。
        int length = in.readInt();

        // 长度如果小于0
        if (length < 0) {
            // 非法数据，关闭连接
            ctx.close();
        }

        if (length > in.readableBytes()) {// 读到的消息体长度如果小于传送过来的消息长度
            // 重置读取位置
            in.resetReaderIndex();
            return null;
        }
        //Logger.cfo("decoder length=" + in.readableBytes());


        byte[] array;
        if (in.hasArray()) {
            //堆缓冲
//            ByteBuf slice = in.slice();
            //小伙伴 calvin 发现的bug，这里指正读取  length 长度
//            ByteBuf slice = in.slice(in.readerIndex(), length);
//            Logger.cfo("slice length=" + slice.readableBytes());

//            array = slice.array();

            array = new byte[length];
            in.readBytes(array, 0, length);
        } else {
            //直接缓冲
            array = new byte[length];
            in.readBytes(array, 0, length);
        }


        // 字节转成对象
        ProtoMsg.Message outmsg =
                ProtoMsg.Message.parseFrom(array);

        return outmsg;
    }
}
