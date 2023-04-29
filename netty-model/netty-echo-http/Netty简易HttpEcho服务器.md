## Netty简易HttpEcho服务器

------



[TOC]



### 实现需求

发送Http请求给Netty服务器，Netty服务器返回请求的请求头、请求方法、URI的参数、body参数。



### 实现所需

JDK、spring、netty



### 实现原理

1. 使用netty监听指定端口
2. channel设置初始化pipline 
   1. 设置HttpRequestDecoder
   
      - 用于将HTTP请求消息解码为HTTPRequest对象，其中包含请求的所有信息，例如URL、HTTP方法、HTTP头和HTTP正文。
   
   2. 设置HttpResponseEncoder
   
      - 用于将HTTPResponse对象编码为HTTP响应消息，其中包含响应的所有信息，例如状态码、HTTP头和HTTP正文。
   
   3. 设置HttpObjectAggregator
   
      - 作用是将多个 Http 消息聚合成一个 FullHttpRequest 或 FullHttpResponse。
   
        在 HTTP 协议中，由于消息体的大小不确定，可能会分为多个消息进行传输。HttpObjectAggregator 可以将这些消息聚合起来，组成完整的消息体，便于后续的处理。
   
   4. 设置自定义的入站处理器
      1. 使用FullHttp做为泛型（因为第3点中设置的HttpObjectAggregator，需要搭配使用）
      2. 读取request的信息





### 实现流程

#### 服务端

1. Netty
   1. 设置serverboostrap parent和children，并绑定
   2. 设置channel类型
   3. 同步绑定端口
   4. channel初始化入站出站设置
      - 设置`HttpRequestDecoder`入站解码
      - 设置`HttpResponseEncoder`出站编码
      - 设置`HttpObjectAggregator`（聚合成 `FullHttpRequest `或 `FullHttpResponse`）
      - 设置自定义入站处理器（`EchoChannelInboundHandler`）
        - `EchoChannelInboundHandler` 继承 `SimpleChannelInboundHandler<FullHttpRequest>`（`SimpleChannelInboundHandler`可以自动释放对象，泛型是因为要配合`HttpObjectAggregator`处理器）
        - 实现抽象方法`com.crazymakercircle.netty.http.echo.HttpEchoHandler#channelRead0`
          1. 先判断读取结果，是否读取成功；如果是读取失败，那么构建返回内容进行返回。
             1. 获取http version
             2. 使用`io.netty.handler.codec.http.DefaultFullHttpResponse#DefaultFullHttpResponse(io.netty.handler.codec.http.HttpVersion, io.netty.handler.codec.http.HttpResponseStatus, io.netty.buffer.ByteBuf)`构造参数，构建一个响应内容。与第7点（响应结果）相同。
          2. 读取request uri
          3. 读取request method
          4. 读取reuqest uil中的请求参数
          5. 处理post请求中的参数内容
             1. 通过`Content-Type`判断请求类型
                1. 普通form表单数据，非multipart形式表单
                2. multipart形式表单
                3. json
                   - 获取fullHttpRequest的content。
                   - 转成字符串。
                   - 转成json对象
          6. 组装回显数据
          7. 响应结果
             1. 判断`ChannelHandlerContext`是否包含属性keep_alive属性
             2. 设置响应length
             3. 如果keep_alive=false,那么在响应头中增加参数（connection:close 头部）
             4. 判断协议版本是否是HTTP 1.0
             5. 如果是HTTP1.0，那么设置keep_alive属性
             6. 发送内容(writeAndFlush)
             7. 如果keep_alive=false,那么发送完成之后，主动关闭连接。
   5. 获取channel，处理channel的close情况
   6. 优雅关闭  
