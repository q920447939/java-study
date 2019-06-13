/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月13日
 */
package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;

/**
 * ClassName: NIOClient
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月13日
 */
public class NIOTest02 {

    static class Client {
        public static void main(String[] args) throws IOException {
            System.out.println("客户端启动了....");
            //1.创建socker
            SocketChannel channel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));

            //2.切换异步模式  jdk1.7以上
            channel.configureBlocking(false);

            //3.创建缓冲区大小(使用直接缓冲)
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

            //将数据放入到缓冲区中
            buffer.put(new Date().toString().getBytes());

            //4.切换到写数据
            buffer.flip();

            //5.写入数据
            channel.write(buffer);

            //6.关闭通道和清理缓冲区
            channel.close();
            buffer.clear();
        }
    }

    /**
     * @Description:NIO 服务端<br>
     *     大概的实现思路的
     *     1,首先注册到选择器上面去(选择器此时是设置为可接受的)
     *     2,选择器轮训客户端
     *     3,如果有客户端往选择器上面发数据,检查client是否已经发送完毕
     *     4,检测到发送完毕,把选择器设置为可读状态(也就是选择器中现在有可以读的数据了)
     *     5,处理选择器中可以读的数据
     *
     *     选择器流程:
     *     接受数据--> 设置可读-->处理数据
     * @param:
     * @return:
     * @auther: liming
     * @date: 2019/6/13 16:55
     */
    static class Server {
        public static void main(String[] args) throws IOException {
            System.out.println("服务端启动了....");
            //1.创建服务通道
            ServerSocketChannel socketChannel = ServerSocketChannel.open();

            //2.设置异步读取
            socketChannel.configureBlocking(false);

            //3.绑定连接
            socketChannel.bind(new InetSocketAddress(8888));

            //4.获取选择器
            Selector selector = Selector.open();

            //5.将通道注册到选择器中 "并且监听已经接受到的事件"
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);

            //6.轮训已经准备好的事件(也就是client的数据已经发送给select)
            while (selector.select() > 0) {
                //7.判断选择器接收到的数据和定义的数据类型一样(SelectionKey.OP_ACCEPT)
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    //8.获取准备就绪的事件
                    SelectionKey sk = iterator.next();
                    //9.只知道选择器中现在有SelectionKey.OP_ACCEPT的事件,但是不知道是否接收已经完成
                    if (sk.isAcceptable()) {
                        //10.已经接收完成
                        SocketChannel socketChanFinish = socketChannel.accept();
                        socketChanFinish.configureBlocking(false);
                        //11.注册到select 设置读取状态
                        socketChanFinish.register(selector, SelectionKey.OP_READ);

                    } else if (sk.isReadable()) {//12.可读状态,那么就可以真正的读取数据了
                        //13.强转为SocketChannel(供后面读取数据)
                        SocketChannel channel = (SocketChannel)sk.channel();
                        //14.创建buff缓冲区(不能使用直接缓冲区,会报错,原因未知)
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int len = 0;
                        //15.如果读取到的数据长度>0
                        while ((len = channel.read(byteBuffer)) > 0) {
                            //16.开启读取模式
                            byteBuffer.flip();
                            System.out.println("limit:"+byteBuffer.limit());
                            //17.打印读取到的值
                            System.out.println(new String(byteBuffer.array(),0,len));

                            //18.清理缓冲区
                            byteBuffer.clear();
                        }
                    }
                    iterator.remove();
                }
            }
        }

    }
}
