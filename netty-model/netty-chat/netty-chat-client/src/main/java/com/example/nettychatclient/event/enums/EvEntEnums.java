package com.example.nettychatclient.event.enums;

import lombok.Getter;

/**
 * ClassName: EvEntEnums
 *
 * @author leegoo
 * @Description:
 * @date 2023年04月06日
 */
@Getter
public enum EvEntEnums {
    LOGIN(0, "登录"),
    CHAT(1, "聊天");
    private int eventType;
    private String desc;

    EvEntEnums(int eventType, String desc) {
        this.eventType = eventType;
        this.desc = desc;
    }
}
