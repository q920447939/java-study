/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年04月29日
 */
package cn.withmes.nettyechohttp;

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

/**
 * ClassName: HttpHelp
 * @Description:
 * @author leegoo
 * @date 2023年04月29日
 */
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
