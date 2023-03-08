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
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ClassName: NIOReactorClientV2
 * @Description:
 * @author leegoo
 * @date 2023年03月07日
 */
public class NIOReactorClientV2 {

    private static AtomicBoolean isCanSend = new AtomicBoolean(false);
    private static String SEND_MESSAGE = "";

    public static class Product implements  Runnable{

        private static final org.slf4j.Logger log = LoggerFactory.getLogger(Product.class);

        private static final Scanner sc = new Scanner(System.in);
        private AtomicBoolean isFirst = new AtomicBoolean(true);

        @Override
        public void run() {
            log.info("【生产者】 请输入需要发送的消息");
            while (true) {
                boolean hasNext = sc.hasNext();
                if (!hasNext){
                    continue;
                }
                String message = sc.next();
                if (null == message || "".equals(message)) {
                    log.info("【生产者】 消息不能为空！");
                    continue;
                }
                if (isFirst.get()) {
                    SEND_MESSAGE = message;
                    isFirst.set(false);
                    isCanSend.set(true);
                    continue;
                }
                SEND_MESSAGE = message;
                isCanSend.set(true);
                log.info("【生产者】 生产消息完毕,message={}", message);
                log.info("【生产者】 请输入需要发送的消息");
            }
        }
    }


    public static class Consumer implements  Runnable{
        private static final org.slf4j.Logger log = LoggerFactory.getLogger(Consumer.class);

        private static ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        @Override
        public void run() {
            try {
                //select
                Selector selector = Selector.open();
                //channel  //ip
                SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",18899));

                socketChannel.configureBlocking(false);
                //register
                socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
                //infinish
                while (!socketChannel.finishConnect()) {

                }
                log.info("【消费者】 远程连接成功");
                //loop
                while (!Thread.interrupted()) {
                    int select = selector.select(1000);
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    if (null == selectionKeys || selectionKeys.isEmpty()) {
                        continue;
                    }
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    while (it.hasNext()) {
                        SelectionKey sk = it.next();
                        //delete
                        it.remove();
                        //isWrite
                        if (sk.isWritable() && isCanSend.get()) {
                            byteBuffer.put(SEND_MESSAGE.getBytes(StandardCharsets.UTF_8));
                            byteBuffer.flip();
                            socketChannel.write(byteBuffer);
                            byteBuffer.clear();
                            isCanSend.set(false);
                        }
                        if (sk.isReadable()) {
                            //isRead
                            int len = 0;
                            while ( (len = socketChannel.read(byteBuffer)) > 0){
                                byteBuffer.flip();
                                log.info("【消费者】 接收到的数据 {}", new String(byteBuffer.array(), 0, len));
                                byteBuffer.clear();
                            }

                        }

                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args) {
        new Thread(new Product(),"product").start();
        new Thread(new Consumer(),"consumer").start();
    }
}
