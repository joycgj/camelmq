package camel.tc.cmq.netty;

import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import camel.tc.cmq.protocol.CommandCode;
import camel.tc.cmq.protocol.Datagram;
import camel.tc.cmq.protocol.RemotingCommand;
import camel.tc.cmq.util.RemotingBuilder;

import java.util.Map;
import java.util.concurrent.ExecutorService;

// done
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<RemotingCommand> {

    private static final Logger LOG = LoggerFactory.getLogger(NettyServerHandler.class);

    private final Map<Short, NettyRequestExecutor> commands = Maps.newHashMap();

    void registerProcessor(short requestCode, NettyRequestProcessor processor, ExecutorService executor) {
        this.commands.put(requestCode, new NettyRequestExecutor(requestCode, processor, executor));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RemotingCommand command) {
        command.setReceiveTime(System.currentTimeMillis());
        processMessageReceived(ctx, command);
    }

    private void processMessageReceived(ChannelHandlerContext ctx, RemotingCommand cmd) {
        if (cmd != null) {
            switch (cmd.getCommandType()) {
                case REQUEST_COMMAND:
                    processRequestCommand(ctx, cmd);
                    break;
                case RESPONSE_COMMAND:
                    processResponseCommand(ctx, cmd);
                    break;
                default:
                    break;
            }
        }
    }

    private void processResponseCommand(final ChannelHandlerContext ctx, final RemotingCommand cmd) {
    }

    private void processRequestCommand(ChannelHandlerContext ctx, RemotingCommand cmd) {
        final NettyRequestExecutor executor = commands.get(cmd.getHeader().getCode());
        if (executor == null) {
            cmd.release();
            LOG.error("unknown command code, code: {}", cmd.getHeader().getCode());
            Datagram response = RemotingBuilder.buildEmptyResponseDatagram(CommandCode.UNKNOWN_CODE, cmd.getHeader());
            ctx.writeAndFlush(response);
        } else {
            executor.execute(ctx, cmd);
        }
    }
}
