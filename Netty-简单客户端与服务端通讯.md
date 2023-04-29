## Netty-简单客户端与服务端通讯

类描述：

| 类名                   | 描述         |
| ---------------------- | ------------ |
| NettyEchoServerV1      | 服务端配置类 |
| NettyEchoClientV1      | 客户端配置类 |
| NettyEchoServerHandler | 服务端处理类 |
| NettyEchoClientHandler | 客户端处理类 |



先看服务端配置类

```java
public class NettyEchoServerV1 {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NettyEchoServerV1.class);

    public void startServer() {
          /*
        1.创建serverboot
        2.创建parent,children
        3.把serverboost绑定parent,children ，绑定IP , 设置channel类型
        4.设置参数  keepalieve
        5.设置children初始化 -> 设置pipline
        6.获取channel
        7.注册是否channel完成事件
        8.channel关闭事件
        9.parent,children 优雅关闭
         */
        EventLoopGroup parent = null, children = null;
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            parent = new NioEventLoopGroup();//负责轮训
            children = new NioEventLoopGroup();//负责事件处理
            serverBootstrap.group(parent, children);
            serverBootstrap.channel(NioServerSocketChannel.class);//设置管道类型
            serverBootstrap.localAddress(18899);
            //设置一些参数
            serverBootstrap.option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);
            serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    //设置管道处理类。链式处理
                    ch.pipeline().addLast(NettyEchoServerHandler.INSTANCE);
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("服务管道建立成功！");
                    log.info("服务管道建立成功！,绑定端口={}", channelFuture.channel().localAddress());
                }
            });
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception ex) {
            log.info("netty 服务端异常", ex);
        } finally {
            if (null != parent) {
                parent.shutdownGracefully();
            }
            if (null != children) {
                children.shutdownGracefully();
            }
        }
    }

    public static void main(String[] args) {
        new NettyEchoServerV1().startServer();
    }
}

```



服务端处理类

```java
@ChannelHandler.Sharable //这个注解是证明这个类是可以共享的，如果是没有成员变量，可以设置。类似于单例模式
public class NettyEchoServerHandler extends ChannelInboundHandlerAdapter {
    public static final NettyEchoServerHandler INSTANCE = new NettyEchoServerHandler();

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NettyEchoServerHandler.class);


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
}
```



客户端配置

```java
public class NettyEchoClientV1 {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NettyEchoClientV1.class);

    public void start(){
        /*
        1.创建boostrap
        2.创建workEventLoop
        3.把workEventLoop绑定到boostrap上
        4.绑定远程ip和端口
        5.设置boostrap参数
        6.设置管道类型
        7.设置children初始化事件 -> pipline.addLast  ->@Share
        8.while循环进行连接判断， 没有连接的话使用awaitUnitnter..
        9.连接成功。
        10.创建Scan,while循环判断HasNext
        11.取出next
        12.分配内存 是否可以使用内存增加 减少对象创建？
        13.注册回调事件
        14.转成字符串 utf8 -> 然后writeAndFlush
        15.优雅关闭
         */
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup workEventLoopGroup = new NioEventLoopGroup();
        try {
            bootstrap.group(workEventLoopGroup);
            bootstrap.remoteAddress("127.0.0.1",18899);
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(NettyEchoClientHandler.INSTANCE);
                }
            });
            ChannelFuture channelFuture = bootstrap.connect();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    log.info("[NettyEchoClientV1] 连接服务端成功");
                }
            });
            while (!channelFuture.isSuccess()) {
                channelFuture.awaitUninterruptibly();
            }
            Channel channel = channelFuture.channel();
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String next = scanner.next();
                ByteBuf buffer = channel.alloc().buffer();
                buffer.writeBytes(next.getBytes(StandardCharsets.UTF_8));
                ChannelFuture writeAndFlush = channel.writeAndFlush(buffer);
                writeAndFlush.addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("[NettyEchoClientV1] 发送数据【{}】成功",next);
                    }else {
                        log.info("[NettyEchoClientV1] 发送数据失败");
                    }
                });
            }
        } catch (Exception ex) {
            log.error("[NettyEchoClientV1] 处理异常",ex);
        }finally {
            workEventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyEchoClientV1().start();
    }
}
```





客户端处理类

```java
@ChannelHandler.Sharable
public class NettyEchoClientHandler extends ChannelInboundHandlerAdapter {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NettyEchoClientHandler.class);

    public static final NettyEchoClientHandler INSTANCE = new NettyEchoClientHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        int len = byteBuf.readableBytes();
        byte [] arr = new byte[len];
        byteBuf.getBytes(0,arr);
        log.info("[NettyEchoClientHandler] 读取到的数据={}",new String(arr, StandardCharsets.UTF_8));
        byteBuf.release();
        super.channelRead(ctx, msg);
    }
}
```





先启动服务端，再启动客户端

客户端结果：

```java
[nioEventLoopGroup-2-1] INFO  cn.withmes.netty.v1.client.NettyEchoClientV1  - [NettyEchoClientV1] 连接服务端成功
666 //发送的字符串
[nioEventLoopGroup-2-1] INFO  cn.withmes.netty.v1.client.NettyEchoClientV1  - [NettyEchoClientV1] 发送数据【666】成功
[nioEventLoopGroup-2-1] INFO  cn.withmes.netty.v1.client.NettyEchoClientHandler  - [NettyEchoClientHandler] 读取到的数据=hello666
```



服务端结果：

```java
服务管道建立成功！
[nioEventLoopGroup-2-1] INFO  cn.withmes.netty.v1.server.NettyEchoServerV1  - 服务管道建立成功！,绑定端口=/0:0:0:0:0:0:0:0:18899
[nioEventLoopGroup-3-1] INFO  cn.withmes.netty.v1.server.NettyEchoServerHandler  - [NettyEchoServerHandler] msg类型=直接内存
[nioEventLoopGroup-3-1] INFO  cn.withmes.netty.v1.server.NettyEchoServerHandler  - [NettyEchoServerHandler] 读取到的数据=[666]
[nioEventLoopGroup-3-1] INFO  cn.withmes.netty.v1.server.NettyEchoServerHandler  - [NettyEchoServerHandler] 写回后，msg.refCnt:1
```

