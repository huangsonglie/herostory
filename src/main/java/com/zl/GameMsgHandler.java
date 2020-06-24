package com.zl;

import com.google.protobuf.GeneratedMessageV3;
import com.zl.cmdhandler.*;
import com.zl.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgHandler.class);

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (userId == null) return;
        UserManager.remove(userId);
        Broadcaster.removeChannel(ctx.channel());
        super.handlerRemoved(ctx);
        GameMsgProtocol.UserQuitResult.Builder userQuitResultBuilder = GameMsgProtocol.UserQuitResult.newBuilder();
        userQuitResultBuilder.setQuitUserId(userId);
        GameMsgProtocol.UserQuitResult userQuitResult = userQuitResultBuilder.build();
        Broadcaster.broadcast(userQuitResult);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (ctx == null) return;
        super.channelActive(ctx);
        Broadcaster.addChannel(ctx.channel());
    }

//    @Override
//    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
//        super.handlerRemoved(ctx);
//    }

    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.info("服务器收到消息, msg={}", msg);

        ICmdHandler<? extends GeneratedMessageV3> iCmdHandler = CmdFactory.create(msg);
        iCmdHandler.handler(ctx, cast(msg));

    }

    private <TCmd extends GeneratedMessageV3> TCmd cast(Object msg) {
        if (msg == null) return null;
        return (TCmd) msg;
    }
}
