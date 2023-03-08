/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月02日
 */
package cn.withmes.nioSend;

import cn.withmes.NioDemoConfig;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;

/**
 * ClassName: NioSendClient
 *
 * @author leegoo
 * @Description:
 * @date 2023年03月02日
 */
public class NioSendServer {
    private final static org.slf4j.Logger log = LoggerFactory.getLogger(NioSendServer.class);



    public static void main(String[] args) throws IOException {
        //1.创建一个选择器
        Selector selector = Selector.open();

        //2.创建一个管道
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);
        //监听ip和端口
        socketChannel.bind(new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_PORT));

        //3.把管道注册到选择器上
        SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //4.选择器发现有 选择事件
        while (selector.select() > 0){
            //5. 迭代事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey sk = iterator.next();
                //6.如果是连接事件 那么把当前管道注册为可读可写可连接
                if (sk.isAcceptable()){
                    log.info("发生了新连接，{}",sk.channel());
                    SocketChannel channel = socketChannel.accept();
                    channel.configureBlocking(false);
                    channel.register(selector,SelectionKey.OP_READ );
                }
                //7 读到可写状态
                if (sk.isWritable()){
                    log.info("写就绪事件");
                }
                if (sk.isConnectable()){
                    log.info("可以连接");
                }
                if (sk.isReadable()){
                    //8 读到可读状态
                    SocketChannel channel = (SocketChannel) sk.channel();
                    try {
                        //9.分配字节缓存对象
                        ByteBuffer  buffer = ByteBuffer.allocate(1024);
                        int len = 0;
                        //10. 从管道读取字节
                        while ( (len = channel.read(buffer)) > 0 ){
                            buffer.flip();
                            byte[] array = buffer.array();
                            System.out.println("读取到的数据：buffer=" + Arrays.toString(array));
                            buffer.clear();
                        }
                    } catch (IOException e) {
                        log.error("IO异常",e);
                    }finally {
                        //channel.close();
                    }
                }
                //NIO的特点是只会累加，已选择键的集合不会删除
//如果不删除，下一次又会被select()函数选中
                iterator.remove();
            }
        }
        socketChannel.close();
    }

}
