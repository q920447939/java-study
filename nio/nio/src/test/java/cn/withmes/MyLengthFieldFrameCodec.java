package cn.withmes;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.nio.ByteOrder;
import java.util.List;

public class MyLengthFieldFrameCodec extends io.netty.channel.ChannelInitializer<io.netty.channel.Channel> {

    private static final int MAX_FRAME_LENGTH = 1024 * 1024;
    private static final int LENGTH_FIELD_LENGTH = 4;
    private static final int LENGTH_FIELD_OFFSET = 0;
    private static final int LENGTH_ADJUSTMENT = 0;
    private static final int INITIAL_BYTES_TO_STRIP = 4;

    @Override
    protected void initChannel(io.netty.channel.Channel ch) throws Exception {
        ch.pipeline()
          .addLast(new LengthFieldBasedFrameDecoder(ByteOrder.BIG_ENDIAN, MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP, true))
          .addLast(new MessageToMessageDecoder<ByteBuf>() {
              @Override
              protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
                  // Decode your message here
                  out.add(msg);
              }
          })
          .addLast(new LengthFieldPrepender(LENGTH_FIELD_LENGTH, true))
          .addLast(new MessageToByteEncoder<ByteBuf>() {
              @Override
              protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
                  // Encode your message here
                  out.writeBytes(msg);
              }
          });
    }
}
