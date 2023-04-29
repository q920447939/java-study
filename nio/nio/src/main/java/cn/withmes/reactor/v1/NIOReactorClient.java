/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月04日
 */
package cn.withmes.reactor.v1;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
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
 * ClassName: NIOReactorServer
 *
 * @author leegoo
 * @Description: 生产消费模型
 * 分两个线程
 * produce 线程  专门监听 控制台输入字符  -> 将结果存到全局发送字符串中。 并且把可发送状态改为true
 * consumer 线程
 * 建立socketchannel 连接
 * 不断轮训可发送状态 是否为true，当为true时。发送消息（byteBuffer此时为可写模式）  ，随后把byteBuffer 改为可读模式 并接受server返回结果
 * @date 2023年03月04日
 */
public class NIOReactorClient {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NIOReactorClient.class);


    private static AtomicBoolean sendStatus = new AtomicBoolean(false);

    private static String SEND_STR = "";


    public static class Produce implements Runnable {
        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            log.info("请输入要发送的内容");
            String input = scanner.nextLine();
            log.info("【生产者】 首次获取到键盘输入信息:{}", input);
            SEND_STR = input;
            sendStatus.set(true);
            while (true) {
                input = scanner.nextLine();
                if (sendStatus.get()) {
                    log.info("【生产者】 前一个发送消息未完成:{}", SEND_STR);
                    continue;
                }
                log.info("【生产者】 获取到键盘输入信息:{}", input);
                SEND_STR = input;
                sendStatus.set(true);
            }
        }
    }

    public static class Consumer implements Runnable {
        private SocketChannel socketChannel;
        private Selector selector;
        private SelectionKey sk;
        private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        private AtomicBoolean isFirst = new AtomicBoolean(true);

        public Consumer() throws Exception {
            //select
            selector = Selector.open();
            //channel
            //ip port
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 18899));
            //no block
            socketChannel.configureBlocking(false);
            socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
            while (!socketChannel.finishConnect()) {
                Thread.sleep(1000L);
            }
            sk = socketChannel.register(selector,
                    SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            log.info("【消费者】 建立连接完成");
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if (isFirst.get()) {
                        if (Thread.interrupted() || !sendStatus.get()) {
                            //log.info("【消费者】前一个发送状态未完成");
                            try {
                                Thread.sleep(1000L);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            continue;
                        }
                        log.info("【消费者】进入可发送状态");
                        byteBuffer.put(SEND_STR.getBytes(StandardCharsets.UTF_8));
                        byteBuffer.flip();
                        try {
                            socketChannel.write(byteBuffer);
                            byteBuffer.clear();
                            log.info("【消费者】发送消息【{}】完成", SEND_STR);
                            sendStatus.set(false);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        isFirst.set(false);
                        continue;
                    }
                    Selector selector1 = sk.selector();
                    selector1.select(1000);
                    Set<SelectionKey> selectionKeys = selector1.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    while (it.hasNext()) {
                        SelectionKey sk = it.next();
                        if (sk.isWritable()) {
                            if (Thread.interrupted() || !sendStatus.get()) {
                                //log.info("【消费者】前一个发送状态未完成");
                                try {
                                    Thread.sleep(1000L);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }

                            } else {
                                log.info("【消费者】进入可发送状态");
                                byteBuffer.put(SEND_STR.getBytes(StandardCharsets.UTF_8));
                                byteBuffer.flip();
                                try {
                                    socketChannel.write(byteBuffer);
                                    byteBuffer.clear();
                                    log.info("【消费者】发送消息【{}】完成", SEND_STR);
                                    sendStatus.set(false);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                        }
                        if (sk.isReadable()) {
                            int len = 0;
                            try {
                                while ((len = socketChannel.read(readBuffer)) > 0) {
                                    readBuffer.flip();
                                    log.info("【消费者】 接收到的数据 {}", new String(readBuffer.array(), 0, len));
                                    readBuffer.clear();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            log.info("请输入要发送的内容");
                        }
                        it.remove();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new Thread(new Produce(), "produce").start();
        new Thread(new Consumer(), "consumer").start();
    }
}
