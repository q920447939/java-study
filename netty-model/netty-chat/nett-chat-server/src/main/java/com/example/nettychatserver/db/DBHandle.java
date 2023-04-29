/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年04月03日
 */
package com.example.nettychatserver.db;

import com.example.nettychat.common.common.bean.User;
import com.example.nettychatserver.session.ServerSession;

import java.util.Map;

/**
 * ClassName: DBHandle
 * @Description:
 * @author leegoo
 * @date 2023年04月03日
 */
public class DBHandle {
    private static final Map<String, ServerSession> SESSION_MAP = new java.util.concurrent.ConcurrentHashMap<>();

    public static boolean isLogin(User user) {
        if (SESSION_MAP.size() == 0) {
            return false;
        }
        for (ServerSession session : SESSION_MAP.values()) {
            User sessionUser = (User) session.getUser();
            if (sessionUser.getUid().equals(user.getUid()) && sessionUser.getPlatform().equals(user.getPlatform()) ) {
                return true;
            }
        }
        return false;
    }
}
