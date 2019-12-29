package camel.tc.cmq.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

// done
@ChannelHandler.Sharable
class ConnectionHandler extends ChannelInboundHandlerAdapter {

    private final ConnectionEventHandler handler;

    ConnectionHandler(final ConnectionEventHandler handler) {
        this.handler = handler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        handler.channelActive(ctx);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        handler.channelInactive(ctx);
        super.channelInactive(ctx);
    }
}
