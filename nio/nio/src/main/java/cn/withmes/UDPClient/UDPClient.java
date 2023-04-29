/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月02日
 */
package cn.withmes.UDPClient;

import cn.withmes.Dateutil;
import cn.withmes.NioDemoConfig;
import cn.withmes.nioSend.NioSendClient;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Scanner;

/**
 * ClassName: UDPClient
 *
 * @author leegoo
 * @Description:
 * @date 2023年03月02日
 */
public class UDPClient {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NioSendClient.class);

    public void send() throws IOException {
//获取DatagramChannel
        DatagramChannel dChannel = DatagramChannel.open();
//设置为非阻塞
        dChannel.configureBlocking(false);
        ByteBuffer buffer =
                ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
        Scanner scanner = new Scanner(System.in);
        log.info("UDP客户端启动成功！");
        log.info("请输入发送内容:");
        while (scanner.hasNext()) {
            String next = scanner.next();
            buffer.put((Dateutil.getNow() + " >>" +
                    next).getBytes());
            buffer.flip();
//通过DatagramChannel发送数据
            dChannel.send(buffer, new
                    InetSocketAddress("127.0.0.1", 18899));
            buffer.clear();
        }
//关闭DatagramChannel
        dChannel.close();
    }

    public static void main(String[] args) throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);

        new UDPClient().send();
        Selector selector = Selector.open();
//将通道注册到选择器
        datagramChannel.register(selector,
                SelectionKey.OP_READ);
//通过选择器查询IO事件
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator =
                    selector.selectedKeys().iterator();
            ByteBuffer buffer =
                    ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
//迭代IO事件
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
//可读事件，有数据到来
                if (selectionKey.isReadable()) {
//读取DatagramChannel数据
                    SocketAddress client =
                            datagramChannel.receive(buffer);
                    buffer.flip();
                    log.info(new String(buffer.array(), 0,
                            buffer.limit()));
                    buffer.clear();
                }
            }
            iterator.remove();
        }
    }
}
