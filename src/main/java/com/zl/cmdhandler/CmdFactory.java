package com.zl.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import com.zl.msg.GameMsgProtocol;
import com.zl.util.PackageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
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
        LOGGER.info("=====初始化CmdFactory=====");
        try {
            String packageName = CmdFactory.class.getPackage().getName();
            Set<Class<?>> handlerClazzSet = PackageUtil.listSubClazz(packageName, true, ICmdHandler.class);
            for (Class<?> handlerClazz : handlerClazzSet) {
                if ((handlerClazz.getModifiers() & Modifier.ABSTRACT) != 0) continue;
                Method[] handlerClazzDeclaredMethods = handlerClazz.getDeclaredMethods();
                for (Method method : handlerClazzDeclaredMethods) {
                    if (!"handle".equals(method.getName())) {
                        continue;
                    }
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Class<?> msgType = null;
                    if (parameterTypes.length < 2
                            || parameterTypes[1] == GeneratedMessageV3.class
                            || !GeneratedMessageV3.class.isAssignableFrom(parameterTypes[1])) {
                        continue;
                    }
                    msgType = parameterTypes[1];
                    ICmdHandler<?> handlerClazzNewInstance = (ICmdHandler<?>) handlerClazz.newInstance();
                    LOGGER.info("{} ===> {}", msgType, handlerClazzNewInstance);
                    handlerMap.put(msgType, handlerClazzNewInstance);
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
