package com.zl.cmdhandler;

import com.zl.Broadcaster;
import com.zl.model.MoveState;
import com.zl.model.User;
import com.zl.model.UserDto;
import com.zl.model.UserManager;
import com.zl.msg.GameMsgProtocol;
import com.zl.service.UserService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserEntryCmdHandler implements ICmdHandler<GameMsgProtocol.UserEntryCmd> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEntryCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserEntryCmd cmd) {
        LOGGER.info("UserEntryCmdHandler");
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        User user = UserManager.getUser(userId);
        String heroAvatar = user.heroAvatar;

        GameMsgProtocol.UserEntryResult.Builder builder = GameMsgProtocol.UserEntryResult.newBuilder();
        builder.setUserId(userId);
        builder.setHeroAvatar(heroAvatar);

        GameMsgProtocol.UserEntryResult userEntryResult = builder.build();
        Broadcaster.broadcast(userEntryResult);
    }
}
