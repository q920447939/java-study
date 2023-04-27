/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年04月06日
 */
package com.example.nettychatclient;

import com.example.nettychatclient.event.Event;
import com.example.nettychatclient.event.EventMap;
import com.example.nettychatclient.session.ClientSession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ClassName: CommandHandler
 *
 * @author leegoo
 * @Description:
 * @date 2023年04月06日
 */
@Service
@Slf4j
public class CommandHandler {

    private AtomicBoolean isConnectionServer = new AtomicBoolean(false);
    @Resource
    private ClientConnectionHandler clientConnectionHandler;
    @Resource
    private EventMap eventMap;
    private Scanner scanner = new Scanner(System.in);

    private ClientSession session = null;

    private GenericFutureListener<ChannelFuture> connectedListener  =  new ChannelFutureListener() {
        @Override
        public  void operationComplete(ChannelFuture future) throws Exception {
            Channel channel = future.channel();
            EventLoop eventLoop = channel.eventLoop();
            if (!future.isSuccess()) {
                //todo 重新连接
                log.info("[client] 连接未成功,10秒后将进行重新连接，将进行重新连接");
                eventLoop.schedule(()->{
                    clientConnectionHandler.connection(connectedListener);
                },10, TimeUnit.SECONDS);
            }{
                log.info("client 连接成功 。channel={}",channel);
                session = new ClientSession(channel);
                session.setConnected(true);
                isConnectionServer.set(true);
                notifyCommandThread();
            }
        }
    };

    public synchronized void notifyCommandThread() {
        //唤醒，命令收集程
        this.notify();

    }

    @SneakyThrows
    public synchronized void run(){
        while (true){
            while (!isConnectionServer.get()) {
                //未连接
                FutureTaskScheduler.add(()->{
                    clientConnectionHandler.connection(connectedListener);
                });
                this.wait();
            }
            //已连接
            log.info(eventMap.showAllMenu());

            while (scanner.hasNext()) {
                String input = scanner.next();
                Event event =  eventMap.findEvent(input);
                if (null == event) {
                    break;
                }
                event.operate(input,session);
            }

        }
    }

}
