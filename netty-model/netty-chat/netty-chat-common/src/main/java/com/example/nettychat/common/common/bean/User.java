package com.example.nettychat.common.common.bean;



import com.example.nettychat.common.common.bean.msg.ProtoMsg;
import lombok.Data;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class User {

   private static final AtomicInteger NO=new AtomicInteger(1);
    String uid = String.valueOf(NO.getAndIncrement());
    String devId= UUID.randomUUID().toString();
    String token= UUID.randomUUID().toString();
    String nickName = "nickName";
    PLATTYPE platform = PLATTYPE.WINDOWS;

    // windows,mac,android, ios, web , other
    public enum PLATTYPE {
        WINDOWS, MAC, ANDROID, IOS, WEB, OTHER;
    }

    private String sessionId;


    public void setPlatform(int platform) {
        PLATTYPE[] values = PLATTYPE.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].ordinal() == platform) {
                this.platform = values[i];
            }
        }

    }


    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", devId='" + devId + '\'' +
                ", token='" + token + '\'' +
                ", nickName='" + nickName + '\'' +
                ", platform=" + platform +
                '}';
    }

    public static User fromMsg(ProtoMsg.LoginRequest info) {
        User user = new User();
        user.uid = new String(info.getUid());
        user.devId = new String(info.getDeviceId());
        user.token = new String(info.getToken());
        user.setPlatform(info.getPlatform());
        return user;

    }

}
