package com.zl.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import com.zl.Broadcaster;
import com.zl.model.MoveState;
import com.zl.model.User;
import com.zl.model.UserManager;
import com.zl.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class UserMoveToCmdHandler implements ICmdHandler<GameMsgProtocol.UserMoveToCmd>{

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserMoveToCmd cmd) {
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (userId == null) return;

        GameMsgProtocol.UserMoveToResult.Builder resultBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();
        long moveStartTime = System.currentTimeMillis();
        User user = UserManager.getUser(userId);
        MoveState moveState = new MoveState();
        moveState.moveFromPosX = cmd.getMoveFromPosX();
        moveState.moveFromPosY = cmd.getMoveFromPosY();
        moveState.moveToPosX = cmd.getMoveToPosX();
        moveState.moveToPosY = cmd.getMoveToPosY();
        moveState.moveStartTime = moveStartTime;
        user.moveState = moveState;
        UserManager.updateUser(user);

        resultBuilder.setMoveFromPosX(cmd.getMoveFromPosX());
        resultBuilder.setMoveFromPosY(cmd.getMoveFromPosY());
        resultBuilder.setMoveToPosX(cmd.getMoveToPosX());
        resultBuilder.setMoveToPosY(cmd.getMoveToPosY());
        resultBuilder.setMoveStartTime(moveStartTime);
        resultBuilder.setMoveUserId(userId);
        GameMsgProtocol.UserMoveToResult userMoveToResult = resultBuilder.build();
        Broadcaster.broadcast(userMoveToResult);
    }
}
