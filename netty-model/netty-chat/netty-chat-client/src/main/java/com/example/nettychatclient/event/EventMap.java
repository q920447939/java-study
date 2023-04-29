/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年04月06日
 */
package com.example.nettychatclient.event;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.example.nettychatclient.event.enums.EvEntEnums;
import jakarta.annotation.Resource;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName: EventMap
 *
 * @author leegoo
 * @Description:
 * @date 2023年04月06日
 */
@Setter
@Slf4j
@Component
public class EventMap {
    @Resource
    private List<Event> eventList;

    public String showAllMenu() {
        StringBuilder sb = new StringBuilder("【请选择操作指令】 ");
        for (Event event : eventList) {
            EvEntEnums evEntEnums = event.getType();
            sb.append("\t").append(String.format("| %s  %s ",evEntEnums.getEventType(),evEntEnums.getDesc()));
        }
        return sb.toString();
    }

    public Event findEvent(String key) {
        if (StrUtil.isBlank(key)) {
            log.error("【client】 输入的指令不能为空！");
            return null;
        }
        if (!NumberUtil.isNumber(key)) {
            log.error("【client】 输入的指令不是一个数字！");
            return null;
        }
        int command = Integer.parseInt(key);
        return eventList.stream().filter(k->command == k.getType().getEventType()).findFirst().orElse(null);
    }
}
