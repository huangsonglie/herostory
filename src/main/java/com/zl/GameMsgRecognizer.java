package com.zl;

import com.google.protobuf.GeneratedMessageV3;
import com.zl.msg.GameMsgProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public final class GameMsgRecognizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgRecognizer.class);

    private static final Map<Class<? extends GeneratedMessageV3>, Integer> msgCodeMap = new HashMap<>();


    private GameMsgRecognizer() {

    }

    public static void init() {
        msgCodeMap.put(GameMsgProtocol.UserEntryResult.class, GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE);
        msgCodeMap.put(GameMsgProtocol.WhoElseIsHereResult.class, GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE);
        msgCodeMap.put(GameMsgProtocol.UserMoveToResult.class, GameMsgProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE);
        msgCodeMap.put(GameMsgProtocol.UserQuitResult.class, GameMsgProtocol.MsgCode.USER_QUIT_RESULT_VALUE);
    }

    public static int getMsgCodeByMsg(Object msg) {
        if (msg == null) {
            LOGGER.error("无法支持的消息类型, msgclazz=" + msg.getClass().getName());
            return -1;
        }
        return msgCodeMap.get(msg.getClass());
    }
}
