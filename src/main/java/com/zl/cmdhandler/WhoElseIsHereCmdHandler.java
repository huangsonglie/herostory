package com.zl.cmdhandler;

import com.zl.model.MoveState;
import com.zl.model.User;
import com.zl.model.UserManager;
import com.zl.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;

public class WhoElseIsHereCmdHandler implements ICmdHandler<GameMsgProtocol.WhoElseIsHereCmd> {

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.WhoElseIsHereCmd cmd) {
        GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();
        for (User user : UserManager.getAllUser()) {
            if (user == null) continue;
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
            userInfoBuilder.setUserId(user.userId);
            userInfoBuilder.setHeroAvatar(user.heroAvatar);
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.Builder moveStateBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.newBuilder();
            MoveState moveState = user.moveState;
            moveStateBuilder.setFromPosX(moveState.moveFromPosX);
            moveStateBuilder.setFromPosY(moveState.moveFromPosY);
            moveStateBuilder.setToPosX(moveState.moveToPosX);
            moveStateBuilder.setToPosY(moveState.moveToPosY);
            moveStateBuilder.setStartTime(moveState.moveStartTime);
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState moveStateBuild = moveStateBuilder.build();
            userInfoBuilder.setMoveState(moveStateBuild);
            resultBuilder.addUserInfo(userInfoBuilder);
        }

        GameMsgProtocol.WhoElseIsHereResult whoElseIsHereResult = resultBuilder.build();
        ctx.writeAndFlush(whoElseIsHereResult);
    }
}
