## Netty-概念

概念：

原来`NIO`的`Reactor`模型中，至少会存在两个线程（一个`select`线程还有一个`Handler`线程）

而`netty`把这两个线程换了一个名字，都叫`EventLoopGroup`。

`netty`把原来比较繁琐的代码进行了很多封装性操作（毕竟原来写一个`NIO`的客户端/服务端配置就有很长一大串）。

在服务端：

读操作和写操作被`netty`通过双向链表的方式把绑定起来（为什么是双向链表，是为了链表反过来便遍历的时候方便一点，不存就需要链表反转了，比较麻烦）。

假设现在已经组装了A->B->C链

那么B节点会保存前一个节点（A），和后一个节点（C）

如果是读事件，那么链路方式（代码调用方式）是A->B->C

如果是写事件，那么链路方式（代码调用方式）是C->B->A

![image-20230313172207515.png](https://s2.loli.net/2023/03/14/GzLBoun8l27fJSe.png)
`Head`节点和`Tail`节点是`Netty`自动添加的。

如果想要在读的时终止链路调用。（比如B节点执行后就应该执行C节点）。

只需要重写`io.netty.channel.ChannelInboundHandlerAdapter#channelRead`方法时，不调用`super.channelRead(ctx, msg);`即可

例如：

```java

public class NettyEchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       
        //super.channelRead(ctx, msg); 这个地方注释就不会进行链路的传递了
    }
}
```

调用时序图可参考[Netty-可读链路传递研究](./Netty-可读链路传递研究.md)





同时Netty还使用了**零拷贝技术**

零拷贝技术：

以`Java`语言为例。在传统的读`IO`模型中，首先会将数据（从网卡、磁盘等地方产生的数据）复制到内核中，再从内核中复制到用户态（`JVM`）中。随后作为`Java`语言的使用者才能够使用这些数据。

这样的话会增加一次从内核态到用户态的复制过程。

零拷贝技术实际上是减少内核态到用户态的复制，

假设数据复制到内核态的地址是`0x1234`，用户态是`0x5678`

那么通过零拷贝技术，直接让用户态的读取内核态`0x1234`的数据。这样就减少了一次复制。



Netty 还有`slice`切片实现类似零拷贝的功能。

假设现在有一个数组(取名`a`数组)，里面拥有`[1,2,3,4,5]`。

如果我们从节省内存的角度上拿到`a`数组，那么我们只需要定义一个变量b，等于a的对象地址即可。

`b = a`;

这样就做到了最省内存，因为只是把`b`的引用指向地址改成`a`了。

相比较传统的深层次复制（数据完全复制到一个新的地址）。这种浅层复制虽然不可读。但是减少了内存开销，这就是`Netty`零拷贝（`bytebuffer`,`Composite Buffers`）的大致原理。



[透过现象看 Java AIO 的本质](https://xie.infoq.cn/article/3740980e285510995cd206143)
