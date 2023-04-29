/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年04月06日
 */
package com.example.nettychatclient.event;

import cn.hutool.core.util.StrUtil;
import com.example.nettychat.common.common.bean.User;
import com.example.nettychatclient.event.enums.EvEntEnums;
import com.example.nettychatclient.send.ChatSend;
import com.example.nettychatclient.send.LoginSend;
import com.example.nettychatclient.session.ClientSession;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * ClassName: ChatEvent
 *
 * @author leegoo
 * @Description:
 * @date 2023年04月06日
 */
@Service
@Slf4j
public class ChatEvent implements Event {
    @Resource
    private ChatSend chatSend;

    private final Scanner scanner = new Scanner(System.in);
    @Override
    public EvEntEnums getType() {
        return EvEntEnums.CHAT;
    }

    @Override
    public void operate(String input, ClientSession session) {
        log.info("请输入接收人:消息（例如 张三:你好啊，朋友）");
        String toUser = null;
        String content = null;
        while (scanner.hasNext()){
            String next = scanner.next();
            String[] split = next.split(":");
            if (split.length != 2){
                log.info("输入格式不正确,请重新输入");
                continue;
            }
            toUser = split[0];
            content = split[1];
            if (StrUtil.isBlank(toUser) || StrUtil.isBlank(content)){
                log.info("接收人或消息为空！请从新输入");
                continue;
            }
            log.info("接收人:{},消息:{}",toUser,content);
            break;
        }
        try {
            chatSend.sendChtMsg(session.getUser(),session,toUser,content);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
