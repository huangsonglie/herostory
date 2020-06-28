package com.zl;

import com.google.protobuf.GeneratedMessageV3;
import com.zl.cmdhandler.CmdFactory;
import com.zl.cmdhandler.ICmdHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class GameMsgProcessor {

    private static final GameMsgProcessor instance = new GameMsgProcessor();

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgProcessor.class);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor((runnable)->{
        Thread thread = new Thread(runnable, "mainThread");
        return thread;
    });

    private GameMsgProcessor() {}

    public static GameMsgProcessor getInstance() {
        return instance;
    }

    public void process(ChannelHandlerContext ctx, Object msg) {
        LOGGER.info("服务器收到消息, msg={}", msg);
        executorService.submit(()->{
            try {
                ICmdHandler<? extends GeneratedMessageV3> iCmdHandler = CmdFactory.create(msg);
                iCmdHandler.handle(ctx, cast(msg));
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        });
    }


    private <TCmd extends GeneratedMessageV3> TCmd cast(Object msg) {
        if (msg == null) return null;
        return (TCmd) msg;
    }
}
