package camel.tc.cmq.netty;

import io.netty.channel.ChannelHandlerContext;
import camel.tc.cmq.protocol.Datagram;
import camel.tc.cmq.protocol.RemotingCommand;

import java.util.concurrent.CompletableFuture;

// done
public interface NettyRequestProcessor {

    CompletableFuture<Datagram> processRequest(ChannelHandlerContext ctx, RemotingCommand request);

    boolean rejectRequest();
}
