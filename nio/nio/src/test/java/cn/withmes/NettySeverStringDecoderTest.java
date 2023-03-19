/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月15日
 */
package cn.withmes;

import cn.withmes.netty.encodeDecode.beyeToString.NettyServerStringDecoder;
import cn.withmes.netty.encodeDecode.beyeToString.NettyServerStringPrintDecoder;
import cn.withmes.netty.encodeDecode.byteToInt.NettyServerIntDecoder;
import cn.withmes.netty.encodeDecode.byteToInt.NettyServerIntPrintDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.util.Random;

/**
 * ClassName: NettySeverStringDecoderTest
 * @Description:
 * @author leegoo
 * @date 2023年03月15日
 */
public class NettySeverStringDecoderTest {

    @Test
    public void testNettySeverIntDecoder(){
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new NettyServerStringDecoder());
                ch.pipeline().addLast(new NettyServerStringPrintDecoder());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);


        Random random = new Random();
        for (int j = 0; j < 100; j++) {
            StringBuilder str = new StringBuilder();
            ByteBuf buf = Unpooled.buffer();
            int num = random.nextInt(5);
            num = (num == 0) ? 1 : num;
            for (int i1 = 0; i1 < num; i1++) {
                str.append("学习netty").append("\t");
            }
            str.append("(随机次数)=").append(num);
            byte[] bytes = str.toString().getBytes();
            buf.writeInt(bytes.length);
            buf.writeBytes(bytes);
            channel.writeInbound(buf);
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
