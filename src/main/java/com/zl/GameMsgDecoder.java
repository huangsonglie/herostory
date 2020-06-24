package com.zl;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.zl.msg.GameMsgProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

public class GameMsgDecoder extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof BinaryWebSocketFrame)) return;

        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = frame.content();
        short msgLength = byteBuf.readShort();
        short msgCode = byteBuf.readShort();

        byte[] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);

        GeneratedMessageV3 cmd = null;
        Message.Builder msgBuilder = GameMsgRecognizer.getMsgBuilder(msgCode);
        cmd = (GeneratedMessageV3) msgBuilder.mergeFrom(msgBody).build();

        if (cmd != null) {
            ctx.fireChannelRead(cmd);
        }

    }
}
