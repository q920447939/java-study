
# 什么是Netty ？

> Netty是由Jboss提供的一款著名的开源框架，常用于搭建 RPC中的TCP服务器、Websocket服务器，甚至是类似Tomcat的Web服务器，反正就是各种网络服务器，在处理高并发的项目中，有奇用！功能丰富且性能良好，基于Java中NIO的二次封装，具有比原生NIO更好更稳健的体验。
>
> 关于Netty 原理，请参见 [《Netty Zookeeper Redis 高并发实战》](https://www.cnblogs.com/crazymakercircle/p/11397271.html)  一书



# 为什么要使用 Netty 替代 Tomcat?

很多项目，都需要基于  Websocket 协议做在线客服、在线推送、在线聊天，虽然 Tomcat 内置支持  Websocket  协议，但是由于 Tomcat  的吞吐量、连接数都很低，作为测试是可以的。**在生产环境，一定需要使用高吞吐量、高连接数的 Netty 服务器进行替代**。



![](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9pbWcyMDE4LmNuYmxvZ3MuY29tL2Jsb2cvMTQ4NTM5OC8yMDE4MTAvMTQ4NTM5OC0yMDE4MTAyMjIzMjIxODIzMi0zODAxNzM3OC5qcGc?x-oss-process=image/format,png)

之所以 Netty 性能高，因为其使用的是 Reactor 反应器模式。关于反应器模式原理，请参见 [《Netty Zookeeper Redis 高并发实战》](https://www.cnblogs.com/crazymakercircle/p/11397271.html)  一书。



# Netty+WS 在线聊天（在线推送）功能演示

聊天过程gif 演示：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200421184539870.gif#pic_center)



聊天示意图：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200421184453380.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NyYXp5bWFrZXJjaXJjbGU=,size_16,color_FFFFFF,t_70#pic_center)





# Springboot + Netty + websocket 项目结构
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200421184655623.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NyYXp5bWFrZXJjaXJjbGU=,size_16,color_FFFFFF,t_70#pic_center)


# Netty  服务启动

Netty搭建的服务器基本上都是差不多的写法：

绑定主线程组和工作线程组，这部分对应架构图中的事件循环组。其原理，，请参见 [《Netty Zookeeper Redis 高并发实战》](https://www.cnblogs.com/crazymakercircle/p/11397271.html)  一书。

重点就是ChannelInitializer的配置，以异步的方式启动，最后是结束的时候关闭线程组。

# 下面是用 Springboot + Netty + websocket  实战的逻辑：

- 使用 Json  传递实体消息；

- ServerSession 存储了每个会话，保存对 Channel和 User，使用User 表示连接上来用户

- 前端要求填入用户和房间（群组）后，模拟登录，并返回用户列表。进入后可以发送群组消息。

  

# Springboot + Netty + websocket  实战  的学习重点：

- 报文处理器
- 业务处理器
- 会话的管理

Netty的开发，是有固定套路的，具体请参见 **疯狂创客圈   经典图书 ： 《Netty Zookeeper Redis 高并发实战》   面试必备 +  面试必备 + 面试必备**  

![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9maWxlcy5jbmJsb2dzLmNvbS9maWxlcy9jcmF6eW1ha2VyY2lyY2xlL2Jvb2sxLmdpZg)
