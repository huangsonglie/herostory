package com.zl;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public final class Broadcaster {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private Broadcaster(){

    }

    public static void addChannel(Channel ch) {
        if (ch == null) return;
        channelGroup.add(ch);
    }

    public static void removeChannel (Channel ch){
        if (ch == null) return;
        channelGroup.remove(ch);
    }

    public static void broadcast(Object msg) {
        if (msg == null) return;
        channelGroup.writeAndFlush(msg);
    }
}
