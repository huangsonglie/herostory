package com.zl.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import com.zl.User;
import com.zl.UserManager;
import com.zl.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;

public class WhoElseIsHereCmdHandler implements ICmdHandler<GameMsgProtocol.WhoElseIsHereCmd> {

    @Override
    public void handler(ChannelHandlerContext ctx, GameMsgProtocol.WhoElseIsHereCmd cmd) {
        GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();
        for (User user : UserManager.getAllUser()) {
            if (user == null) continue;
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
            userInfoBuilder.setUserId(user.userId);
            userInfoBuilder.setHeroAvatar(user.heroAvatar);
            resultBuilder.addUserInfo(userInfoBuilder);
        }

        GameMsgProtocol.WhoElseIsHereResult whoElseIsHereResult = resultBuilder.build();
        ctx.writeAndFlush(whoElseIsHereResult);
    }
}
