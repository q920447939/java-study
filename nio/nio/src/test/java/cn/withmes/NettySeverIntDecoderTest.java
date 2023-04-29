/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月15日
 */
package cn.withmes;

import cn.withmes.netty.encodeDecode.byteToInt.NettyServerIntDecoder;
import cn.withmes.netty.encodeDecode.byteToInt.NettyServerIntPrintDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 * ClassName: NettySeverIntDecoderTest
 * @Description:
 * @author leegoo
 * @date 2023年03月15日
 */
public class NettySeverIntDecoderTest {

    @Test
    public void testNettySeverIntDecoder(){
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new NettyServerIntDecoder());
                ch.pipeline().addLast(new NettyServerIntPrintDecoder());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);

        for (int j = 0; j < 100; j++) {
            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(j);
            channel.writeInbound(buf);
        }

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
