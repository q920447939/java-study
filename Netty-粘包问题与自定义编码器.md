## Netty-粘包问题与自定义编码器



### 粘包/半包问题

在TCP协议的连接过程过，系统会采用**Nagle**算法尽量让内核态到用户态这一过程中的交互次数减少。

有时候会将接收到的数据包合在一起发到用户态中。

虽然初衷是好的，但是会引起把多个包的数据合并在一起的问题，或者一次只收到半个包的问题。

比如客户端发送的数据是：

```
第一个包 ： A1
第二个包 ： B2
第三个包 ： C3
```

在我们的认知中，服务端应该收到**3**次包。每一次收到的数据依次是

```
A1
B2
C3
```

但是实际上服务端可能收到的数据包是

```
A1B
2C3  
```

也有可能是

```
A
1B2
C3
```



总之，服务端收到的数据包与实际预期收到的结果不一致！

下面我们通过一个简单的场景模拟粘包问题！

### 场景复现

模拟客户端向服务端发送多次请求的情况。总共2个`class`

| Class                  | 描述         |
| ---------------------- | ------------ |
| NettyEchoClientHandler | 客户端发送类 |
| NettyEchoServer        | 服务端接收类 |



`NettyEchoClientHandler`源码如下：

```java
public class NettyDumpSendClient {

    private int serverPort;
    private String serverIp;
    Bootstrap b = new Bootstrap();

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NettyDumpSendClient.class);


    public NettyDumpSendClient(String ip, int port) {
        this.serverPort = port;
        this.serverIp = ip;
    }

    public void runClient() {
        //创建reactor 线程组
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();

        try {
            //1 设置reactor 线程组
            b.group(workerLoopGroup);
            //2 设置nio类型的channel
            b.channel(NioSocketChannel.class);
            //3 设置监听端口
            b.remoteAddress(serverIp, serverPort);
            //4 设置通道的参数
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            //5 装配子通道流水线
            b.handler(new ChannelInitializer<SocketChannel>() {
                //有连接到达时会创建一个channel
                protected void initChannel(SocketChannel ch) throws Exception {
                    // pipeline管理子通道channel中的Handler
                    // 向子channel流水线添加一个handler处理器
                    //ch.pipeline().addLast(NettyEchoClientHandler.INSTANCE);
                }
            });
            ChannelFuture f = b.connect();
            f.addListener((ChannelFuture futureListener) ->
            {
                if (futureListener.isSuccess()) {
                    log.info("EchoClient客户端连接成功!");

                } else {
                    log.info("EchoClient客户端连接失败!");
                }

            });

            // 阻塞,直到连接完成
            f.sync();
            Channel channel = f.channel();

            //6发送大量的文字
            byte[] bytes = "我爱学netty!".getBytes(Charset.forName("utf-8"));
            Random random = new Random();
            for (int j = 0; j < 100; j++) {
                //1-3之间的随机数
                int num = random.nextInt(3);
                num = (num == 0 ) ? 1 : num;
                ByteBuf buf = Unpooled.buffer();

                //首先 写入头部  head，也就是后面的数据长度

                //buf.writeInt( (bytes.length * num) );

                //然后 写入content

                for (int k = 0; k < num; k++) {
                    buf.writeBytes(bytes);
                }
                channel.writeAndFlush(buf);
            }



            // 7 等待通道关闭的异步任务结束
            // 服务监听通道会一直等待通道关闭的异步任务结束
            ChannelFuture closeFuture =channel.closeFuture();
            closeFuture.sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 优雅关闭EventLoopGroup，
            // 释放掉所有资源包括创建的线程
            workerLoopGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new NettyDumpSendClient("127.0.0.1", 18899).runClient();
    }
}
```



`NettyEchoServer`源码如下：

```java
public class NettyEchoServer {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NettyEchoServerV1.class);

    public void startServer() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup parent = new NioEventLoopGroup(1);
        EventLoopGroup children = new NioEventLoopGroup();
        serverBootstrap.group(parent, children);
        try {
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress(18899);
            serverBootstrap.option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);
            serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf byteBuf = (ByteBuf) msg;
                            log.info("[NettyEchoServerHandler] msg类型={}",byteBuf.hasArray() ? "堆内存":"直接内存");
                            int len = byteBuf.readableBytes();
                            byte [] arr = new byte[len];
                            byteBuf.getBytes(0,arr);
                            String message = new String(arr, StandardCharsets.UTF_8);
                            log.info("[NettyEchoServerHandler] 读取到的数据=[{}]", message);
                            //ChannelFuture future  = ctx.writeAndFlush("服务端回复消息=> hello! ");
                            ByteBuf sendBuff = ctx.channel().alloc().buffer();
                            sendBuff.writeBytes(("hello" + message).getBytes(StandardCharsets.UTF_8));
                            ChannelFuture future  = ctx.writeAndFlush(sendBuff);
                            future.addListener((ChannelFuture futureListener) -> {
                                log.info("[NettyEchoServerHandler] 写回后，msg.refCnt:{}",byteBuf.refCnt());
                            });
                            super.channelRead(ctx, msg);
                        }
                    });
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    log.info("【服务端】连接成功");
                }
            });
            while (!channelFuture.isSuccess()) {
                channelFuture.awaitUninterruptibly();
            }
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception ex) {
            log.error("【服务端】异常",ex);
        }finally {
            parent.shutdownGracefully();
            children.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyEchoServer().startServer();
    }
}
```



先启动`NettyEchoServer`主程序，再启动`NettyDumpSendClient`主程序。

随后观察`NettyEchoServer`控制台输出结果：

```java
[NettyEchoServerHandler] 读取到的数据=[��学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱学netty!我爱�]
[NettyEchoServerHandler] 读取到的数据=[�netty!我爱学netty!]
```

我们可以看到读取到的数据`我爱学netty!`有时候一次打印了很多次（在客户端中，我们通过产生一个随机数，让客户端一次可能发送多次`我爱学netty!`，最多不超过3次），有时候只打印了1-2次，同时还产生了乱码。

### 解决方式

1. 服务端每次一定要读取到固定长度的字节后才认为读取完毕。
2. 服务端每次读取到特定分隔符才认为读取完毕（比如遇到`\r ,\n`）。
3. 客户端发送字节时，发送`head`和`body`概念，在`head`中，写入`body`的字节长度；在服务端中，先解析`head`获取到`body`的长度，随后再解析`body`长度的内容。

### Netty解码器

在服务端中，我们的主要工作基本上可以分为`获取到字节数据->业务逻辑处理1->业务逻辑处理2->结束`。

但是如果我们在`业务逻辑处理1->业务逻辑处理2`中；如果每次都对字节数据进行一遍解析，这样会无形的增加代码量（大量重复解析操作），同时在每次的业务逻辑处理中相同的操作同样会浪费更多的性能和增加服务器的负担从而降低的服务端的总体处理耗时。

netty通过解码器的抽象模型（双向链表结构）。具体可看[Netty-可读链路传递研究](./Netty-可读链路传递研究.md)

总而言之，Netty提供给开发者方便的代码实现来扩展解码器。

在前面的服务端代码中，我们通过`serverBootstrap.childHandler`方法，重写`initChannel`方法中增加解码器调用链

`ch.pipeline().addLast(xxx)`。

`xxx`代表了自定义的实现



### 主要解码器以及场景（编码器类似流程）

除了我们可以自定义实现编码器/解码器之外，Netty框架也为我们提供了一些比较常用的编码器/解码器

| 编码器                       | 作用                                                         |
| ---------------------------- | ------------------------------------------------------------ |
| ByteToMessageDecoder         | 将字节流解码为消息对象。                                     |
| MessageToByteEncoder         | 将消息对象编码为字节流。                                     |
| StringEncoder                | 将字符串编码为字节流。                                       |
| StringDecoder                | 将字节流解码为字符串。                                       |
| LengthFieldBasedFrameDecoder | 基于长度字段的帧解码器，先读取一个长度字段，然后根据长度字段读取后续的字节流。 |



| 解码器                       | 作用                                                         |
| ---------------------------- | ------------------------------------------------------------ |
| ByteToMessageDecoder         | 将字节流解码为消息对象。                                     |
| LengthFieldBasedFrameDecoder | 基于长度字段的帧解码器，先读取一个长度字段，然后根据长度字段读取后续的字节流。 |
| DelimiterBasedFrameDecoder   | 基于分隔符的帧解码器，根据指定的分隔符将接收到的数据划分为一帧。 |
| LineBasedFrameDecoder        | 基于换行符的帧解码器，将接收到的数据按行划分。               |
| ProtobufDecoder              | 将protobuf编码的字节流解码为protobuf消息对象。               |
| HttpObjectDecoder            | 将HTTP请求解码为HTTP请求消息对象。                           |
| WebSocketFrameDecoder        | 将WebSocket帧解码为WebSocketFrame消息对象。                  |

### 

### 通过`LengthFieldBasedFrameDecoder`解决Netty粘包问题

客户端我们需要把`数据长度+数据内容`结合在一起发送给服务端。

例如（16进制表示）：

```
长度					内容
0xC					  1 2 3 4 5 6 7 8 1 2 3 4

C在16进制中表示12,所以内容应该占12个字节
```



在我们上面的例子中，客户端配置内容没有做其他调整。在发送数据过程中我们稍作改动(具体可参考`cn.withmes.netty.encodeDecode.lengthFieldBasedFrame.lengthAdjustment0.NettyDumpSendClient`)。

```java
//6发送大量的文字
Random random = new Random();
for (int i = 0; i < 10; i++) {
    StringBuffer sb = new StringBuffer();
    //发送ByteBuf
    ByteBuf buffer = channel.alloc().buffer();
    //1-3之间的随机数
    int num = random.nextInt(5);
    num = (num == 0) ? 1 : num;
    for (int i1 = 0; i1 < num; i1++) {
        sb.append("我爱学netty!").append("\t");
    }
    sb.append("(随机次数)=").append(num);

    //首先 写入头部  head，也就是后面的数据长度
    String str = sb.toString();
    log.info("[Clinet] 发送数据={}",str);
    byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
    buffer.writeInt( (bytes.length ) );
    buffer.writeBytes(bytes);
    channel.writeAndFlush(buffer);
}
```





在服务端中，我们把`LengthFieldBasedFrameDecoder`放入到解码器链的头部（让`LengthFieldBasedFrameDecoder`第一个做处理）。具体可参考`cn.withmes.netty.encodeDecode.lengthFieldBasedFrame.lengthAdjustment0.NettyEchoServer`。

```java
serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024,0,4,0,4));
        pipeline.addLast(new StringDecoder(StandardCharsets.UTF_8));
        pipeline.addLast(new NettyServerPrintDecoder());
    }
});
```

因为`LengthFieldBasedFrameDecoder`构造器有很多个，我们挑选一个上面代码的例子做一个讲解。

```java
LengthFieldBasedFrameDecoder(
            int maxFrameLength, //最大接收数量
            int lengthFieldOffset, //头的起始位置
    	    int lengthFieldLength,//头的占用字节长度
            int lengthAdjustment, //调整参数，暂时忽略
    	    int initialBytesToStrip // 需要跳过的字节长度
)
    
```



在客户端中，我们首先写入4个字节的数据（代表了`body`的长度），再写入不定长的`body`数据。**一次性**发送给服务端。

在服务端中，`pipeline.addLast(new LengthFieldBasedFrameDecoder(1024,0,4,0,4));`

这段代码代表的含义是：

1. 第一个参数：最大允许获取长度是1024个字节。
2. 第二个参数：我们知道`head`是在首位，所以我们填0。
3. 第三个参数：客户端写入的是4个字节长苏，所以我们填写4。
4. 第四个参数：填0。
5. 第五个参数：因为客户端发送的数据结构中，最开始的4个字节长度，我们是不需要的。所以我们要跳过最前面的4个字节。所以我们填4。

​	

先启动服务端，在启动客户端。随后观察服务端的打印是否正常。

​	

当然还有`LengthFieldBasedFrameDecoder`的其他场景，这里只举例了一个最简单的场景。

在另外的一个场景中

客户端第一步填充2个字节的`魔数`，第二步4个字节填充数据长度（注意数据长度=业务标识+数据内容长度），第三步填充8个字节业务标识，第三步填充数据内容。

服务端的`LengthFieldBasedFrameDecoder`对应设置为：

​	`new LengthFieldBasedFrameDecoder(1024,2,4,0,14)`

1. 第一个参数1024，表示最大接收数据为1024个字节。
2. 第二个参数为2，表示长度域从第三个字节开始（第1个和第2个字节被`魔数`代替）
3. 第三个参数为4，表示数据长度的长度为4个字节。
4. 第四个参数填0 。不需要调整。
5. 第五个参数为14，因为**魔数**（2个字节）+**数据长度**（4个字节）+**业务标识**（8个字节） = 14。

具体的代码可参考`cn.withmes.netty.encodeDecode.lengthFieldBasedFrame.lengthAdjustment2.NettyDumpSendClient`和`cn.withmes.netty.encodeDecode.lengthFieldBasedFrame.lengthAdjustment2.NettyEchoServer`



