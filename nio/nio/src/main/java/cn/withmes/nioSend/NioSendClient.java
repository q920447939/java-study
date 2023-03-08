/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月02日
 */
package cn.withmes.nioSend;

import cn.withmes.NioDemoConfig;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * ClassName: NioSendClient
 *
 * @author leegoo
 * @Description:
 * @date 2023年03月02日
 */
public class NioSendClient {
    private final org.slf4j.Logger log = LoggerFactory.getLogger(NioSendClient.class);


    static byte[] bytes = new byte[]{
            0x0,0x1,0x2,0x3,0x4,0x5
    };

    public static void main(String[] args) throws IOException {
        //分配byte缓存字节
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        for (byte aByte : bytes) {
            //写入测试数据
            byteBuffer.put(aByte);
        }
        //将byteBuffer变成可读状态
        byteBuffer.flip();

        InetSocketAddress address =
                new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP,
                        NioDemoConfig.SOCKET_SERVER_PORT);
        //socket 管道绑定到对应的ip和端口
        SocketChannel socketChannel = SocketChannel.open(address);
        //改为非阻塞状态
        socketChannel.configureBlocking(false);
        if (socketChannel.isConnected()){
            System.out.println(" isConnected ....");
        }
        //一直尝试连接远程socket
        while (!socketChannel.finishConnect()){
            System.out.println("....");
        }
        //连接成功开始写入数据
        socketChannel.write(byteBuffer);
        //终止写入数据
        socketChannel.shutdownOutput();
        socketChannel.close();
        byteBuffer.clear();
    }
}
