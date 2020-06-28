package com.zl.cmdhandler;

import com.zl.Broadcaster;
import com.zl.GameProp;
import com.zl.model.User;
import com.zl.model.UserManager;
import com.zl.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAttackCmdHandler implements ICmdHandler<GameMsgProtocol.UserAttkCmd> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAttackCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserAttkCmd userAttkCmd) {

        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        int attkSubHp = GameProp.attkSubHp;

        int targetUserId = userAttkCmd.getTargetUserId();
        User targetUser = UserManager.getUser(userId);
        targetUser.currHp -= attkSubHp;

        GameMsgProtocol.UserAttkResult.Builder userAttkResultBuilder = GameMsgProtocol.UserAttkResult.newBuilder();
        userAttkResultBuilder.setAttkUserId(userId);
        userAttkResultBuilder.setTargetUserId(targetUserId);
        GameMsgProtocol.UserAttkResult userAttkResult = userAttkResultBuilder.build();
        Broadcaster.broadcast(userAttkResult);

        GameMsgProtocol.UserSubtractHpResult.Builder userSubtractHpResultBuilder = GameMsgProtocol.UserSubtractHpResult.newBuilder();
        userSubtractHpResultBuilder.setSubtractHp(attkSubHp);
        userSubtractHpResultBuilder.setTargetUserId(targetUserId);
        GameMsgProtocol.UserSubtractHpResult userSubtractHpResult = userSubtractHpResultBuilder.build();
        Broadcaster.broadcast(userSubtractHpResult);

        if (targetUser.currHp <= 0) {
            GameMsgProtocol.UserDieResult.Builder userDieResultBuilder = GameMsgProtocol.UserDieResult.newBuilder();
            userDieResultBuilder.setTargetUserId(targetUserId);
            GameMsgProtocol.UserDieResult userDieResult = userDieResultBuilder.build();
            Broadcaster.broadcast(userDieResult);
        }

        LOGGER.info("{} attack {}", userId, targetUserId);
    }
}