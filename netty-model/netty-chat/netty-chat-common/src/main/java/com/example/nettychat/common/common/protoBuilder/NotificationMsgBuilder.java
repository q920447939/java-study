package com.example.nettychat.common.common.protoBuilder;


import com.example.nettychat.common.common.bean.msg.ProtoMsg;

public class NotificationMsgBuilder
{

    public static ProtoMsg.Message buildNotification(String json)
    {
        ProtoMsg.Message.Builder mb = ProtoMsg.Message.newBuilder()
                .setType(ProtoMsg.HeadType.MESSAGE_NOTIFICATION);   //设置消息类型


        //设置应答流水，与请求对应
        ProtoMsg.MessageNotification.Builder rb =
                ProtoMsg.MessageNotification.newBuilder()
                        .setJson(json);
        mb.setNotification(rb.build());
        return mb.build();
    }


}
