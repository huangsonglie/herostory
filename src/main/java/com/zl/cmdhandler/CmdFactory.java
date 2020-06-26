package com.zl.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import com.zl.msg.GameMsgProtocol;
import com.zl.util.PackageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class CmdFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmdFactory.class);

    private static final Map<Class<?>, ICmdHandler> handlerMap = new HashMap<>();

    private CmdFactory() {

    }

    public static void init() {
        //  _handlerMap.put(GameMsgProtocol.UserEntryCmd.class, new UserEntryCmdHandler());
        /*try {
            Class<?>[] declaredClazzList = GameMsgProtocol.class.getDeclaredClasses();
            for (Class<?> clazz : declaredClazzList) {
                if (GeneratedMessageV3.class.isAssignableFrom(clazz)) continue;
                String simpleName = clazz.getSimpleName();
                if (!simpleName.endsWith("Cmd")) continue;
                String classFullName = "com.zl.cmdhandler." + simpleName + "Handler";
                handlerMap.put(clazz, (ICmdHandler<?>) Class.forName(classFullName).newInstance());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }*/
        LOGGER.info("=====初始化CmdFactory=====");
        try {
            String packageName = CmdFactory.class.getPackage().getName();
            Set<Class<?>> handlerClazzSet = PackageUtil.listSubClazz(packageName, true, ICmdHandler.class);
            for (Class<?> handlerClazz : handlerClazzSet) {
                if ((handlerClazz.getModifiers() & Modifier.ABSTRACT) != 0) continue;
                ICmdHandler<?> handlerClazzNewInstance = (ICmdHandler<?>) handlerClazz.newInstance();
                String handlerClazzSimpleName = handlerClazz.getSimpleName();
                String cmdClassSimpleName = handlerClazzSimpleName.substring(0, handlerClazzSimpleName.length() - 7);
                Class<?>[] declaredClazzList = GameMsgProtocol.class.getDeclaredClasses();
                for (Class<?> clazz : declaredClazzList) {
                    if (!GeneratedMessageV3.class.isAssignableFrom(clazz)) continue;
                    String simpleName = clazz.getSimpleName();
                    if (!simpleName.equals(cmdClassSimpleName)) continue;
                    LOGGER.info("{} ===> {}", clazz, handlerClazzNewInstance);
                    handlerMap.put(clazz, handlerClazzNewInstance);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    public static ICmdHandler<? extends GeneratedMessageV3> create(Object msg) {
        return handlerMap.get(msg.getClass());
    }
}
