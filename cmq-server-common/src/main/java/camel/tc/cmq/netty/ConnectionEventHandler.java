package camel.tc.cmq.netty;

import io.netty.channel.ChannelHandlerContext;

// done
public interface ConnectionEventHandler {

    void channelActive(ChannelHandlerContext ctx);

    void channelInactive(ChannelHandlerContext ctx);
}
