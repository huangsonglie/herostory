package com.zl.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import com.zl.msg.GameMsgProtocol;

public final class CmdFactory {

    private CmdFactory() {

    }

    public static ICmdHandler<? extends GeneratedMessageV3> create(Object msg) {
        if (msg instanceof GameMsgProtocol.UserEntryCmd) {
            return new UserEntryCmdHandler();
        } else if (msg instanceof GameMsgProtocol.WhoElseIsHereCmd) {
            return new WhoElseIsHereCmdHandler();
        } else if (msg instanceof GameMsgProtocol.UserMoveToCmd) {
            return new UserMoveToCmdHandler();
        }
        return null;
    }
}
