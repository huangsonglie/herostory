package com.zl;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.zl.msg.GameMsgProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public final class GameMsgRecognizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgRecognizer.class);


    private static final Map<Integer, GeneratedMessageV3> msgMap = new HashMap<>();

    private static final Map<Class<? extends GeneratedMessageV3>, Integer> msgCodeMap = new HashMap<>();


    private GameMsgRecognizer() {

    }

    public static void init() {
        msgMap.put(GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE, GameMsgProtocol.UserEntryCmd.getDefaultInstance());
        msgMap.put(GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE, GameMsgProtocol.WhoElseIsHereCmd.getDefaultInstance());
        msgMap.put(GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE, GameMsgProtocol.UserMoveToCmd.getDefaultInstance());

        msgCodeMap.put(GameMsgProtocol.UserEntryResult.class, GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE);
        msgCodeMap.put(GameMsgProtocol.WhoElseIsHereResult.class, GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE);
        msgCodeMap.put(GameMsgProtocol.UserMoveToResult.class, GameMsgProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE);
        msgCodeMap.put(GameMsgProtocol.UserQuitResult.class, GameMsgProtocol.MsgCode.USER_QUIT_RESULT_VALUE);
    }

    public static Message.Builder getMsgBuilder (int msgCode) {
        if (msgCode < 0) return null;
        GeneratedMessageV3 generatedMessageV3 = msgMap.get(msgCode);
        if (generatedMessageV3 == null) return null;
        return generatedMessageV3.newBuilderForType();
    }

    public static int getMsgCodeByMsg(Object msg) {
        if (msg == null) {
            LOGGER.error("无法支持的消息类型, msgclazz=" + msg.getClass().getName());
            return -1;
        }
        return msgCodeMap.get(msg.getClass());
    }
}
