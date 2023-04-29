/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月04日
 */
package cn.withmes.reactor.v1;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * ClassName: NIOReactorServer
 * select
 * channel bind
 * register -> accept
 * getread
 * write client
 *
 * @author leegoo
 * @Description:
 * @date 2023年03月04日
 */
public class NIOReactorServer implements Runnable {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NIOReactorServer.class);

    private final Selector selector;
    private ServerSocketChannel socketChannel;

    public NIOReactorServer() throws IOException {
        //select
        // channel bind
        //register -> accept
        selector = Selector.open();
        socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.bind(new InetSocketAddress("127.0.0.1", 18899));
        SelectionKey sk = socketChannel.register(selector, 0, new AcceptHandler(selector, socketChannel));
        sk.interestOps(SelectionKey.OP_ACCEPT);
        log.info("【reactor server】启动成功");
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                selector.select(1000);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            if (null == selectionKeys || selectionKeys.size() == 0) {
                continue;
            }
            Iterator<SelectionKey> it = selectionKeys.iterator();
            while (it.hasNext()) {
                SelectionKey selectionKey = it.next();
                log.info("【reactor server】客户端连接成功,{}", selectionKey.channel());
                Runnable r = (Runnable) selectionKey.attachment();
                it.remove();
                r.run();
            }
        }
    }


    public static class AcceptHandler implements Runnable {
        private Selector selector;
        private ServerSocketChannel socketChannel;

        public AcceptHandler(Selector selector, ServerSocketChannel socketChannel) {
            this.selector = selector;
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            try {
                SocketChannel channel = socketChannel.accept();
                log.info("【reactor server】接收到客户端数据 {}", channel);
                if (channel != null)
                    new EchoHandler(selector, channel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class EchoHandler implements Runnable {
        final SocketChannel channel;
        final SelectionKey sk;
        final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        static final int RECIEVING = 0, SENDING = 1;
        int state = RECIEVING;
        String receiveMessage = "";

        EchoHandler(Selector selector, SocketChannel c) throws IOException {
            channel = c;
            //设置为非阻塞模式
            c.configureBlocking(false);
            //仅仅取得选择键，绑定事件处理器
            // 后设置感兴趣的IO事件
            sk = channel.register(selector, 0);

            //将Handler作为选择键的附件
            sk.attach(this);

            //第二步,注册Read就绪事件
            sk.interestOps(SelectionKey.OP_READ);
        }

        @Override
        public void run() {
            try {

                if (state == RECIEVING) {
                    int len = 0;
                    while ((len = channel.read(byteBuffer)) > 0) {
                        receiveMessage = new String(byteBuffer.array(), 0, len);
                        log.info("【reactor server】 接收到的数据 {}", receiveMessage);
                    }
                    byteBuffer.flip();
                    sk.interestOps(SelectionKey.OP_WRITE);
                    state = SENDING;
                } else if (state == SENDING) {
                    //channel.write(byteBuffer);
                    //byteBuffer.clear();
                    //回复客户端
                    String message = "answer: " + receiveMessage ;
                    byte[] responseByte = message.getBytes();
                    ByteBuffer writeBuffer = ByteBuffer.allocate(responseByte.length);
                    writeBuffer.put(responseByte);
                    writeBuffer.flip();
                    channel.write(writeBuffer);
                    state = RECIEVING;
                    sk.interestOps(SelectionKey.OP_READ);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                sk.cancel();
                try {
                    channel.finishConnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(new NIOReactorServer()).start();
    }
}
