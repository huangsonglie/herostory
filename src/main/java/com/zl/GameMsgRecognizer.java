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

    private static final Map<Class<?>, Integer> msgCodeMap = new HashMap<>();


    private GameMsgRecognizer() {

    }

    public static void init() {
        try {
            Class<?>[] declaredClazzList = GameMsgProtocol.class.getDeclaredClasses();
            for (Class<?> declaredClazz : declaredClazzList) {
                if (!GeneratedMessageV3.class.isAssignableFrom(declaredClazz)) continue;
                String simpleName = declaredClazz.getSimpleName();
                String lowerCaseSimpleName = simpleName.toLowerCase();
                GameMsgProtocol.MsgCode[] msgCodes = GameMsgProtocol.MsgCode.values();
                for (GameMsgProtocol.MsgCode msgCode : msgCodes) {
                    String msgCodeName = msgCode.name();
                    msgCodeName = msgCodeName.toLowerCase().replace("_", "");
                    if (msgCodeName.equals(lowerCaseSimpleName)) {
                        if (msgCodeName.indexOf("cmd") != -1) {
                            msgMap.put(msgCode.getNumber(), (GeneratedMessageV3) declaredClazz.getDeclaredMethod("getDefaultInstance").invoke(declaredClazz));
                        } else if (msgCodeName.indexOf("result") != -1) {
                            msgCodeMap.put(declaredClazz, msgCode.getNumber());
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }


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
