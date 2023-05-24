package com.crazymaker.websocket.Web;

import com.crazymaker.websocket.Model.User;
import com.crazymaker.websocket.session.SessionMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by Chaofan at 2018/7/6 9:39
 * email:chaofan2685@qq.com
 **/

@RestController
@RequestMapping("/ws")
@Slf4j
public class SocketController
{
    public static Map<Long, String> img = new HashMap();

    /**
     * 根据房间号获得其中的用户
     *
     * @param room 房间号
     * @return
     */
    @RequestMapping("/online")
    public Map<String, Object> online(String room)
    {
        Map<String, Object> result = new HashMap<>();
        Set<User> rooms = SessionMap.inst().getGroupUsers(room);
//        List<String> nicks = new ArrayList<>();
        List<Map<String, String>> users = new ArrayList<>();
        if (rooms != null)
        {
            rooms.forEach(user ->
            {
                Map<String, String> map = new HashMap<>();
                map.put("nick", user.getNickname());
                map.put("id", user.getId());
                users.add(map);
            });
            result.put("onlineNum", rooms.size());
            result.put("onlineUsera", users);
        } else
        {
            result.put("onlineNum", 0);
            result.put("onlineUsera", null);
        }
        return result;
    }

    /**
     * 判断昵称在某个房间中是否已存在，房间是否有密码，如果有，用户输入的密码又是否正确
     *
     * @param room 房间号
     * @param nick 昵称
     * @param pwd  密码
     * @return
     */
    @RequestMapping("/judgeNick")
    public Map<String, Object> judgeNick(String room, String nick, String pwd)
    {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        Set<User> rooms = SessionMap.inst().getGroupUsers(room);

        if (rooms != null)
        {
            rooms.forEach(user ->
            {
                if (user.getNickname().equals(nick))
                {
                    result.put("code", 1);
                    result.put("msg", "昵称已存在，请重新输入");
                    log.debug("有重复");
                }
            });
            if ((Integer) result.get("code") != 0)
            {
                return result;
            }
            result.put("code", 3);
            result.put("msg", "房间无密码");
            return result;
        }
        return result;
    }


    /**
     * 获取所有房间
     *
     * @return
     */
    @RequestMapping("/allRoom")
    public Map<String, Object> allRoom()
    {
        Map<String, Object> result = new HashMap<>();
        List<String> rooms = SessionMap.inst().getGroupNames();
        result.put("rooms", rooms);
        return result;
    }

}
