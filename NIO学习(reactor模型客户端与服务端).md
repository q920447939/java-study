## NIO学习(reactor模型客户端与服务端)

为了更好的理解`Reactor`模型。我们先不涉及到NIO的知识讲解一下`Reactor`模型的工作原理。这样有利于理解`NIO`的的`Reactor`模型。

**我们可以理解为接收事情和处理事情不是同一个人。**

**接收事情的人把工作接收后，后续的工作对接就交给具体负责的人。**

**有点类似于项目经理拿到项目后，会把具体的事情交给开发人员。同时后续的工作也会交给全权交给开发人员负责进行对接！**

下面我们用一段代码把这个场景描述出来：

```java

/**
 * 工作接口。指派人工
 */
public interface Job {
    /**
     * 做工作
     * @param handler 工作助手
     */
    void doJob(Handler handler);
}
```

```java
/**
 包工头工作
 */
public class Boss implements Job {

    @Override
    public void doJob(Handler handler) {
        System.out.println("包工头开始工作");
        handler.setJob(new Worker());
    }
}


```

```java
/**
 苦逼工人工作
 */
public class Worker implements Job{
    @Override
    public void doJob(Handler handler) {
        System.out.println("工人开始工作");
    }
}

```



```java
/**
 * 工作助手
 */
public class Handler {
    private Job job;

    public void setJob(Job job) {
        this.job = job;
    }

    public void doJob(){
        this.job.doJob(this);
    }
}

```

测试类

```java
public class Test {

    public static void main(String[] args) {
        Handler handler = new Handler();
        handler.setJob(new Boss());
        handler.doJob(); // 包工头开始工作
        handler.doJob(); //工人开始工作
    }
}
```



当一开始指定工作由`Boss`完成时，`Boss`打印了`包工头开始工作`，随后就把工作交给`Worker`处理。所以后续再调用`handler.doJob();`，都是调用的`Worker`类。这与后续的`sk.attach(this);`原理类似！





这里我们详细了解NIO并提供一个例子来讲解。

首先我们先了解服务端主要工作

1. 创建一个`select`。
2. 创建一个`channel`。
3. `channel`设置为非阻塞状态并且绑定IP。
4. 把`channel`注册到具体的`select`上面去，并且注册感兴趣的事件（只有注册了感兴趣的事件，`select`才知道要把什么事件分发到`channel`上）。 

客户端的工作和服务端类似。不过客户端是主动去连接服务端。



下面通过一个详细的例子来说明`reactor`模式。



总体设计架构图

![image-20230307150826014.png](https://s2.loli.net/2023/03/08/zGw8V37XF4cnKpZ.png)


客户端流程图
![image-20230307162628847.png](https://s2.loli.net/2023/03/08/mp6nRKUMHy7TWwq.png)


服务端流程图

![image-20230307164859127.png](https://s2.loli.net/2023/03/08/MZ8JKrLIgXxEo9W.png)





服务端代码：

```java
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
        //1.注册select
        this.selector = Selector.open();
        //2.注册channel      
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        //3.set not block
        socketChannel.configureBlocking(false);
         //4.bind
        socketChannel.bind(new InetSocketAddress("127.0.0.1", 18899));
        //5.register  interest  attem
        SelectionKey sk = socketChannel.register(selector, 0, new AcceptHandler(selector, socketChannel));
        //6.注册感兴趣事件。可接收
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
            //7.loop sk
            Iterator<SelectionKey> it = selectionKeys.iterator();
            while (it.hasNext()) {
                SelectionKey sk = it.next();
                log.info("【服务端】客户端连接成功,{}", sk.channel());
                it.remove();
                if (null != sk) {
                    Runnable r = (Runnable) sk.attachment();
                    //8.转发给acceptHandler
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
                    // 9.转发给echoHandler
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
            //10.设置为非阻塞模式
            socketChannel.configureBlocking(false);
            //11.仅仅取得选择键，绑定事件处理器
            // 12.后设置感兴趣的IO事件
            sk = socketChannel.register(selector, 0);

            //13.将Handler作为选择键的附件
            sk.attach(this);

            //14.第二步,注册Read就绪事件
            sk.interestOps(SelectionKey.OP_READ);
        }

        @Override
        public void run() {
            //run method
            try {
                //15.处理read
                if (state == receive) {
                    int len = 0;
                    while ((len = socketChannel.read(byteBuffer)) > 0) {
                        log.info("【服务端】 接收到的数据 {}", new String(byteBuffer.array(), 0, len));
                    }
                    byteBuffer.flip();
                    this.sk.interestOps(SelectionKey.OP_WRITE);
                    state = send;
                } else if (state == send) {
                    //16.处理回复消息
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

```



客户端代码：

```java
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
 * @Description:  Product专门生产消息 ，consumer 绑定服务器连接，以及消费生产者消息、推送消息给服务端、接收服务端返回的消息
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
```



Client控制台效果

```java
[product] INFO  cn.withmes.reactor.v2.NIOReactorClientV2$Product  - 【生产者】 请输入需要发送的消息
[consumer] INFO  cn.withmes.reactor.v2.NIOReactorClientV2$Consumer  - 【消费者】 远程连接成功
2
[consumer] INFO  cn.withmes.reactor.v2.NIOReactorClientV2$Consumer  - 【消费者】 接收到的数据 [server] => 36
3
[product] INFO  cn.withmes.reactor.v2.NIOReactorClientV2$Product  - 【生产者】 生产消息完毕,message=3
[product] INFO  cn.withmes.reactor.v2.NIOReactorClientV2$Product  - 【生产者】 请输入需要发送的消息
[consumer] INFO  cn.withmes.reactor.v2.NIOReactorClientV2$Consumer  - 【消费者】 接收到的数据 [server] => 32
```

Server控制台效果

```java
[main] INFO  cn.withmes.reactor.v2.NIOReactorServerV2  - 【服务端】启动成功
[main] INFO  cn.withmes.reactor.v2.NIOReactorServerV2  - 【服务端】客户端连接成功,sun.nio.ch.ServerSocketChannelImpl[/127.0.0.1:18899]
[main] INFO  cn.withmes.reactor.v2.NIOReactorServerV2$AcceptHandler  - 【服务端】接收到客户端数据 java.nio.channels.SocketChannel[connected local=/127.0.0.1:18899 remote=/127.0.0.1:61241]
[main] INFO  cn.withmes.reactor.v2.NIOReactorServerV2  - 【服务端】客户端连接成功,java.nio.channels.SocketChannel[connected local=/127.0.0.1:18899 remote=/127.0.0.1:61241]
[main] INFO  cn.withmes.reactor.v2.NIOReactorServerV2$EchoHandler  - 【服务端】 接收到的数据 2
[main] INFO  cn.withmes.reactor.v2.NIOReactorServerV2  - 【服务端】客户端连接成功,java.nio.channels.SocketChannel[connected local=/127.0.0.1:18899 remote=/127.0.0.1:61241]
[main] INFO  cn.withmes.reactor.v2.NIOReactorServerV2  - 【服务端】客户端连接成功,java.nio.channels.SocketChannel[connected local=/127.0.0.1:18899 remote=/127.0.0.1:61241]
[main] INFO  cn.withmes.reactor.v2.NIOReactorServerV2$EchoHandler  - 【服务端】 接收到的数据 3
```





多线程Reactor

总体设计：

1.创建一个selet专门处理accpet事件（取名baseSelect）、创建多个select集合专门处理Read/Write事件(取名workSelects)

2.baseSelect获取到接收事件后，委派给workSelects进行处理

架构图（接收事件）

![image-20230308110259331](F:\liming\work_space\my_work_space\java-study\img\nio\reactor\image-20230308110259331.png)



架构图（工作事件）

![image-20230308110434679](F:\liming\work_space\my_work_space\java-study\img\nio\reactor\image-20230308110434679.png)
