/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年04月29日
 */
package cn.withmes.nettyechohttp;

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
