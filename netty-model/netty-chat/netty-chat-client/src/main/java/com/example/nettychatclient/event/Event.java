/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年04月06日
 */
package com.example.nettychatclient.event;

import com.example.nettychatclient.event.enums.EvEntEnums;
import com.example.nettychatclient.session.ClientSession;

/**
 * ClassName: Event
 * @Description:
 * @author leegoo
 * @date 2023年04月06日
 */
public interface Event {
    EvEntEnums getType();

    void  operate(String input, ClientSession session);

}
