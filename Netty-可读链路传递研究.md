## Netty-可读链路传递研究

继承`io.netty.channel.ChannelInboundHandlerAdapter`并重载`channelRead`方法后。打上断点。

![image-20230314102754457.png](https://s2.loli.net/2023/03/14/uIMvlVcephwZJDA.png)
<center>图1</center>



![image-20230314102859056.png](https://s2.loli.net/2023/03/14/UbdGHLmVjgE8J2z.png)
<center>图2</center>

堆栈信息如上图2所示。

先看到`io.netty.channel.DefaultChannelPipeline#fireChannelRead`，图2方框处。





![image-20230314103057555.png](https://s2.loli.net/2023/03/14/HzrJk97I1GFMETA.png)
<center>图3</center>

图3中可以看到`netty`一开始是传的`head`节点。



![image-20230314103424064.png](https://s2.loli.net/2023/03/14/B9b3ThkjYuHwERK.png)
<center>图4</center>

`head`节点会调用`io.netty.channel.AbstractChannelHandlerContext#invokeChannelRead(java.lang.Object)`（也可以在图2中看到调用链路图）

图4中我们可以看到`io.netty.channel.DefaultChannelPipeline.HeadContext`是`io.netty.channel.ChannelInboundHandler`的实现类。



```java
@Override
public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ctx.fireChannelRead(msg);
}
```



在`HeadContext`实现类中。我们看到他是调用了`ctx.fireChannelRead(msg);`

![image-20230314105854084.png](https://s2.loli.net/2023/03/14/3SBMUQ6aTPWXFLD.png)
这个方法是`Head`的父抽象类实现了。





```java
@Override
public ChannelHandlerContext fireChannelRead(final Object msg) {
    invokeChannelRead(findContextInbound(), msg);
    return this;
}

private AbstractChannelHandlerContext findContextInbound() {
    AbstractChannelHandlerContext ctx = this;
    do {
        ctx = ctx.next;
    } while (!ctx.inbound);
    return ctx;
}
```

在`AbstractChannelHandlerContext#fireChannelRead`抽象类中。首先调用了`findContextInbound()`

`findContextInbound()`方法中，通过`do while` 链路的传递（通过`ctx = ctx.next;`改变了`ctx`的地址）。

所以，如果我们需要进行链路的传递。我们只需要重写`io.netty.channel.ChannelInboundHandlerAdapter#channelRead`方法中调用`super.channelRead(ctx, msg);`

时序图如下：

![image-20230314145213368.png](https://s2.loli.net/2023/03/14/LqPlz2kEKRpnFhf.png)
6 7 流程根据自定义的链路长度会循环多次。
