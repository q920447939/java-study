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



### 代码部分

#### 类大纲

| 类名                     | 作用             |
| ------------------------ | ---------------- |
| HttpHelp                 | 封装HTTP响应     |
| NettyEchoHttpApplication | 主程序           |
| NettyEchoServer          | netty服务配置类  |
| NettyEchoServerHandler   | 自定义核心处理类 |



#### 实现细节

##### pom.xml

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.26</version>
        </dependency>

        <dependency>
            <groupId>commons-lang</groupId>
            <artifactId>commons-lang</artifactId>
            <version>2.6</version>
        </dependency>

        <dependency>
            <groupId>io.netty</groupId>
            <artifactId>netty-all</artifactId>
            <version>4.1.6.Final</version>
        </dependency>

        <!--hutool-->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.6.6</version>
        </dependency>
        <!--引入fastjson  -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.83</version>
        </dependency>

    </dependencies>
```



##### HttpHelp

```java

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_0;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class HttpHelp {

    public static final AttributeKey<HttpVersion> PROTOCOL_VERSION_KEY =
            AttributeKey.valueOf("PROTOCOL_VERSION");

    public static final AttributeKey<Boolean> KEEP_ALIVE_KEY =
            AttributeKey.valueOf("KEEP_ALIVE_KEY");
    public static void sendError(ChannelHandlerContext ctx, HttpResponseStatus errStatus){
        HttpVersion httpVersion = getHttpVersion(ctx);
        DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(httpVersion, errStatus, Unpooled.copiedBuffer("Failure: " + errStatus + "\r\n", CharsetUtil.UTF_8));
        httpResponse.headers().set("Content-Type", "text/plain; charset=UTF-8");
        sendResponse(httpResponse, ctx);
    }

    /**
     * 发送Json格式的响应
     *
     * @param ctx     上下文
     * @param content 响应内容
     */
    public static void sendJsonContent(ChannelHandlerContext ctx, String content)
    {
        HttpVersion version = getHttpVersion(ctx);
        /**
         * 构造一个默认的FullHttpResponse实例
         */
        FullHttpResponse response = new DefaultFullHttpResponse(
                version, OK, Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
        /**
         * 设置响应头
         */
        response.headers().set("Content-Type", "application/json; charset=UTF-8");
        /**
         * 发送响应内容
         */
        sendResponse(response,ctx );
    }


    private static void sendResponse(FullHttpResponse response, ChannelHandlerContext ctx) {
        final Boolean keepAlive = ctx.channel().attr(KEEP_ALIVE_KEY).get();
        HttpUtil.setContentLength(response, response.content().readableBytes());
        if (null == keepAlive || !keepAlive || !isHTTP_1_0(ctx)) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        }else {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(response);
        if (null != keepAlive && !keepAlive) {
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static HttpVersion getHttpVersion(ChannelHandlerContext ctx)
    {
        HttpVersion version;
        if (isHTTP_1_0(ctx))
        {
            version = HTTP_1_0;
        } else
        {
            version = HTTP_1_1;
        }
        return version;
    }

    public static boolean isHTTP_1_0(ChannelHandlerContext ctx)
    {

        HttpVersion protocol_version =
                ctx.channel().attr(PROTOCOL_VERSION_KEY).get();
        if (null == protocol_version)
        {
            return false;
        }
        if (protocol_version.equals(HTTP_1_0))
        {
            return true;
        }
        return false;
    }
}

```



##### NettyEchoHttpApplication

```java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "cn.withmes.nettyechohttp")
public class NettyEchoHttpApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NettyEchoHttpApplication.class, args);
        NettyEchoServer nettyEchoServer = context.getBean(NettyEchoServer.class);
        nettyEchoServer.startServer();
    }

}

```



##### NettyEchoServer

```java

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class NettyEchoServer {
    @Resource
    private NettyEchoServerHandler nettyEchoServerHandler;

    @SneakyThrows
    public void startServer() {
        //创建bootstrap,并且group
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup parent = new NioEventLoopGroup(1);
        NioEventLoopGroup children = new NioEventLoopGroup();
        serverBootstrap.group(parent,children);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new HttpRequestDecoder());
                pipeline.addLast(new HttpResponseEncoder());
                pipeline.addLast(new HttpObjectAggregator(1024*1024));
                pipeline.addLast(nettyEchoServerHandler);
            }
        });
        serverBootstrap.bind(8081).sync().channel().closeFuture().sync();

    }
}

```



##### NettyEchoServerHandler

```java

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static cn.withmes.nettyechohttp.HttpHelp.sendJsonContent;

@Slf4j
@Service
@ChannelHandler.Sharable
public class NettyEchoServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> implements ApplicationContextAware {

    private static final Map<String,String> STRATEGY_MAP = new HashMap<>();
    static {
        STRATEGY_MAP.put("application/json", "jsonParseStrategy");
        STRATEGY_MAP.put("text/plain", "plainParseStrategy");
    }

    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        log.info("NettyEchoServerHandler channelRead0");
        boolean success = fullHttpRequest.decoderResult().isSuccess();
        log.info("NettyEchoServerHandler channelRead0 success:{}", success);
        if (!success) {
            HttpHelp.sendError(channelHandlerContext, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        Map<String,Object> result = new HashMap<>();
        String uri = fullHttpRequest.uri();
        result.put("uri", uri);
        log.info("NettyEchoServerHandler channelRead0 uri:{}", uri);
        HttpMethod method = fullHttpRequest.method();
        result.put("method", method.name());
        log.info("NettyEchoServerHandler channelRead0 method:{}", method.name());
        //获取请求头
        HttpHeaders headers = fullHttpRequest.headers();
        Iterator<Map.Entry<String, String>> it =
                headers.iterator();
        Map<String,String> head  = new HashMap<>();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            log.info("NettyEchoServerHandler channelRead0 header:{}:{}", entry.getKey(), entry.getValue());
            head.put(entry.getKey(), entry.getValue());
        }
        result.put("headers", head);
        //解析URI里面的参数
        Map<String,String> uriParamsMap = this.parseURIParams(fullHttpRequest.uri());
        result.put("uriParams", uriParamsMap);
        if (io.netty.handler.codec.http.HttpMethod.POST.equals(fullHttpRequest.method())){
            Map<String,String> bodyParamsMap  = null;
            try {
                bodyParamsMap = this.parseBodyParams(fullHttpRequest);
            } catch (IllegalArgumentException e) {
               channelHandlerContext.channel().close();
            }
            result.put("bodyParams", bodyParamsMap);
        }
        sendJsonContent(channelHandlerContext, JSONObject.toJSONString(result));
    }

    private Map<String, String> parseBodyParams(FullHttpRequest fullHttpRequest) {
        String contentType = fullHttpRequest.headers().get("Content-Type");
        String beanName = STRATEGY_MAP.get(contentType);
        if (StrUtil.isBlank(beanName)){
            throw new IllegalArgumentException("不支持的Content-Type:" + contentType);
        }
        ParseStrategy strategy = (ParseStrategy) this.applicationContext.getBean(beanName);
        return strategy.parse(fullHttpRequest);
    }

    private Map<String, String> parseURIParams(String uri) {
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri, Charset.defaultCharset());
        Map<String, List<String>> parameters =
                queryStringDecoder.parameters();
        Map<String, String> result = new HashMap<>();
        for (String key : parameters.keySet()) {
            log.info("NettyEchoServerHandler parseURIParams key:{}", key);
            result.put(key, parameters.get(key).stream().findFirst().orElse(null));
        }
        return result;
    }

    public  interface ParseStrategy {
        Map<String,String> parse(FullHttpRequest fullHttpRequest);
    }

    @Service("jsonParseStrategy")
    public  class JSONParseStrategy implements  ParseStrategy {
        @Override
        public Map<String, String> parse(FullHttpRequest fullHttpRequest) {
            Map<String, String> result = new HashMap<>();
            ByteBuf content = fullHttpRequest.content();
            byte[] bytes = new byte[content.readableBytes()];
            content.readBytes(bytes);
            String str = new String(bytes, Charset.defaultCharset());
            JSONObject jsonObject = JSONObject.parseObject(str);
            for (String key : jsonObject.keySet()) {
                result.put(key, jsonObject.getString(key));
            }
            return result;
        }
    }
    @Service("plainParseStrategy")
    public  class PlainParseStrategy implements  ParseStrategy {
        @Override
        public Map<String, String> parse(FullHttpRequest fullHttpRequest) {
            Map<String, String> result = new HashMap<>();
            ByteBuf content = fullHttpRequest.content();
            byte[] bytes = new byte[content.readableBytes()];
            content.readBytes(bytes);
            String str = new String(bytes, Charset.defaultCharset());
            result.put("context", str);
            return result;
        }
    }
}

```



### 测试

通过使用postMan发送Get请求

例1:发送内容  localhost:8081/asdasdas?username=admin&password=123456

回显内容:

```json
{
    "headers": {
        "Accept": "*/*",
        "Cache-Control": "no-cache",
        "User-Agent": "PostmanRuntime/7.26.8",
        "Connection": "keep-alive",
        "Postman-Token": "3a601a6d-76eb-43f1-acef-38e7b2d30e0e",
        "Host": "localhost:8081",
        "Accept-Encoding": "gzip, deflate, br",
        "Content-Length": "10",
        "Content-Type": "text/plain"
    },
    "method": "GET",
    "uriParams": {
        "password": "123456",
        "username": "admin"
    },
    "uri": "/asdasdas?username=admin&password=123456"
}
```



例2:发送json格式数据   URL :localhost:8081/asdasdas?username=admin&password=123456

body 调整为json 格式 

```json
{
    "nickName":"张三",
    "city":"SH"
}
```



回显内容:

```json
{
    "headers": {
        "Accept": "*/*",
        "Cache-Control": "no-cache",
        "User-Agent": "PostmanRuntime/7.26.8",
        "Connection": "keep-alive",
        "Postman-Token": "a95a4633-fa39-4948-af2a-6e328cab6120",
        "Host": "localhost:8081",
        "Accept-Encoding": "gzip, deflate, br",
        "Content-Length": "47",
        "Content-Type": "application/json"
    },
    "method": "POST",
    "uriParams": {
        "password": "123456",
        "username": "admin"
    },
    "bodyParams": {
        "city": "SH",
        "nickName": "张三"
    },
    "uri": "/asdasdas?username=admin&password=123456"
}
```







### 总结

通过Netty搭建的简易Http回显服务器，可以获取到请求的`uri`,`head`,`methd`,`params`。也可以灵活的返回响应信息、