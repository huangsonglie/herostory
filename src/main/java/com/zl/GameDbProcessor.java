package com.zl;

import com.google.protobuf.GeneratedMessageV3;
import com.zl.cmdhandler.CmdFactory;
import com.zl.cmdhandler.ICmdHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class GameDbProcessor {

    private static final GameDbProcessor instance = new GameDbProcessor();

    private static final Logger LOGGER = LoggerFactory.getLogger(GameDbProcessor.class);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor((runnable)->{
        Thread thread = new Thread(runnable, "dbThread");
        return thread;
    });

    private GameDbProcessor() {}

    public static GameDbProcessor getInstance() {
        return instance;
    }

    public void process(Runnable runnable) {
        executorService.submit(runnable);
    }
}
