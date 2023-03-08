/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月07日
 */
package cn.withmes.reactor.v2;


import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class NIOReactorServerV2 {

    private Selector selector;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NIOReactorServerV2.class);


    public NIOReactorServerV2() throws Exception {
        //select
        this.selector = Selector.open();
        //channel  set not block     //bind
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.bind(new InetSocketAddress("127.0.0.1", 18899));
        //register  interest  attem
        SelectionKey sk = socketChannel.register(selector, 0, new AcceptHandler(selector, socketChannel));
        sk.interestOps(SelectionKey.OP_ACCEPT);
        log.info("【服务端】启动成功");
    }

    public void accept() throws IOException {
        while (!Thread.interrupted()) {
            selector.select(1000);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            if (null == selectionKeys || selectionKeys.isEmpty()) {
                continue;
            }
            //loop sk
            Iterator<SelectionKey> it = selectionKeys.iterator();
            while (it.hasNext()) {
                SelectionKey sk = it.next();
                log.info("【服务端】客户端连接成功,{}", sk.channel());
                it.remove();
                if (null != sk) {
                    Runnable r = (Runnable) sk.attachment();
                    //转发给acceptHandler
                    r.run();
                }

            }
        }
    }

    public static class AcceptHandler implements Runnable {
        private static final org.slf4j.Logger log = LoggerFactory.getLogger(AcceptHandler.class);

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
                log.info("【服务端】接收到客户端数据 {}", channel);
                if (null != channel) {
                    // 转发给echoHandler
                    new EchoHandler(selector, channel);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class EchoHandler implements Runnable {
        private Selector selector;
        private SocketChannel socketChannel;
        private int receive = 1;
        private int send = 2;
        private int state = receive;
        private static final org.slf4j.Logger log = LoggerFactory.getLogger(EchoHandler.class);

        private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        private SelectionKey sk;

        public EchoHandler(Selector selector, SocketChannel socketChannel) throws Exception {
            this.selector = selector;
            this.socketChannel = socketChannel;
            //设置为非阻塞模式
            socketChannel.configureBlocking(false);
            //仅仅取得选择键，绑定事件处理器
            // 后设置感兴趣的IO事件
            sk = socketChannel.register(selector, 0);

            //将Handler作为选择键的附件
            sk.attach(this);

            //第二步,注册Read就绪事件
            sk.interestOps(SelectionKey.OP_READ);
        }

        @Override
        public void run() {
            //run method
            try {
                //read
                if (state == receive) {
                    int len = 0;
                    while ((len = socketChannel.read(byteBuffer)) > 0) {
                        log.info("【服务端】 接收到的数据 {}", new String(byteBuffer.array(), 0, len));
                    }
                    byteBuffer.flip();
                    this.sk.interestOps(SelectionKey.OP_WRITE);
                    state = send;
                } else if (state == send) {
                    //write
                    String sendMessage = "[server] => "  + new Random().nextInt(100);
                    ByteBuffer sendBuffer = ByteBuffer.allocate(sendMessage.length());
                    sendBuffer.put(sendMessage.getBytes(StandardCharsets.UTF_8));
                    sendBuffer.flip();
                    this.socketChannel.write(sendBuffer);
                    state = receive;
                    this.sk.interestOps(SelectionKey.OP_READ);
                }
            } catch (Exception ex) {
                try {
                    if (this.socketChannel.finishConnect()) {
                        this.socketChannel.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public static void main(String[] args) throws Exception {
        NIOReactorServerV2 nioReactorServerV2 = new NIOReactorServerV2();
        nioReactorServerV2.accept();
    }
}
