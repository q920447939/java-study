package com.example.nettychatclient.protoConverter;


import com.example.nettychat.common.common.bean.msg.ProtoMsg;
import com.example.nettychatclient.session.ClientSession;

/**
 * 基础 Builder
 *
 * @author 尼恩 at  疯狂创客圈
 */
public class BaseConverter {

    protected ProtoMsg.HeadType type;
    private long seqId;
    private ClientSession session;

    public BaseConverter(ProtoMsg.HeadType type, ClientSession session) {
        this.type = type;
        this.session = session;
    }

    /**
     * 构建消息 基础部分
     */
    public ProtoMsg.Message buildOuter(long seqId) {

        return getOuterBuilder(seqId).buildPartial();
    }

    public ProtoMsg.Message.Builder getOuterBuilder(long seqId) {
        this.seqId = seqId;

        ProtoMsg.Message.Builder mb =
                ProtoMsg.Message.newBuilder()
                        .setType(type)
                        .setSessionId(session.getSessionId())
                        .setSequence(seqId);
        return mb;
    }

    /**
     * 构建消息 基础部分 的 Builder
     */
    public ProtoMsg.Message.Builder baseBuilder(long seqId) {
        this.seqId = seqId;

        ProtoMsg.Message.Builder mb =
                ProtoMsg.Message
                        .newBuilder()
                        .setType(type)
                        .setSessionId(session.getSessionId())
                        .setSequence(seqId);
        return mb;
    }

}
