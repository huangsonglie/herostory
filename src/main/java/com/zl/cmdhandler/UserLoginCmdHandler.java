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

public class UserLoginCmdHandler implements ICmdHandler<GameMsgProtocol.UserLoginCmd> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserLoginCmd userLoginCmd) {
        String userName = userLoginCmd.getUserName();
        String password = userLoginCmd.getPassword();
        LOGGER.info("userName={}, password={}", userName, password);
        UserService.getInstance().login(userName, password, (userDto) -> {
            GameMsgProtocol.UserLoginResult.Builder userLoginResultBuilder = GameMsgProtocol.UserLoginResult.newBuilder();
            if (userDto != null) {
                LOGGER.info("{} login success", userName);
                ctx.channel().attr(AttributeKey.valueOf("userId")).set(userDto.getUserId());
                userLoginResultBuilder.setUserId(userDto.getUserId());
                userLoginResultBuilder.setHeroAvatar(userDto.getHeroAvatar());
                userLoginResultBuilder.setUserName(userDto.getUserName());

                User user = new User();
                user.userId = userDto.getUserId();
                user.heroAvatar = userDto.getHeroAvatar();
                user.moveState = new MoveState();
                UserManager.addUser(user);
            } else {
                userLoginResultBuilder.setUserId(-1);
            }
            GameMsgProtocol.UserLoginResult userLoginResult = userLoginResultBuilder.build();
            ctx.writeAndFlush(userLoginResult);
            return null;
        });


    }
}
