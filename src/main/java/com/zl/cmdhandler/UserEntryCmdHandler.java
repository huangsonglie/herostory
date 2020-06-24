package com.zl.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import com.zl.Broadcaster;
import com.zl.User;
import com.zl.UserManager;
import com.zl.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class UserEntryCmdHandler implements ICmdHandler<GameMsgProtocol.UserEntryCmd> {

    @Override
    public void handler(ChannelHandlerContext ctx, GameMsgProtocol.UserEntryCmd cmd) {
        int userId = cmd.getUserId();
        String heroAvatar = cmd.getHeroAvatar();

        GameMsgProtocol.UserEntryResult.Builder builder = GameMsgProtocol.UserEntryResult.newBuilder();
        builder.setUserId(userId);
        builder.setHeroAvatar(heroAvatar);

        User user = new User();
        user.userId = userId;
        user.heroAvatar = heroAvatar;
        UserManager.addUser(user);

        ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);

        GameMsgProtocol.UserEntryResult userEntryResult = builder.build();
        Broadcaster.broadcast(userEntryResult);
    }
}
