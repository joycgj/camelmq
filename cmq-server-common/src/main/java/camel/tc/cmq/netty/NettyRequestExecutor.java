package camel.tc.cmq.netty;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import camel.tc.cmq.monitor.QMon;
import camel.tc.cmq.protocol.CommandCode;
import camel.tc.cmq.protocol.Datagram;
import camel.tc.cmq.protocol.RemotingCommand;
import camel.tc.cmq.protocol.RemotingHeader;
import camel.tc.cmq.util.RemotingBuilder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

// done
class NettyRequestExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(NettyRequestExecutor.class);

    private final NettyRequestProcessor processor;
    private final ExecutorService executor;

    NettyRequestExecutor(final short requestCode, final NettyRequestProcessor processor, final ExecutorService executor) {
        this.processor = processor;
        this.executor = executor;
        if (executor != null) {
            if (executor instanceof ThreadPoolExecutor) {
                QMon.executorQueueSizeGauge(String.valueOf(requestCode), () -> (double) ((ThreadPoolExecutor) executor).getQueue().size());
            }
        }
    }

    void execute(final ChannelHandlerContext ctx, final RemotingCommand cmd) {
        if (executor == null) {
            executeWithMonitor(ctx, cmd);
            return;
        }

        try {
            executor.execute(() -> executeWithMonitor(ctx, cmd));
        } catch (RejectedExecutionException e) {
            ctx.writeAndFlush(errorResp(CommandCode.BROKER_ERROR, cmd));
        }
    }

    private void executeWithMonitor(final ChannelHandlerContext ctx, RemotingCommand cmd) {
        final long start = System.currentTimeMillis();
        try {
            doExecute(ctx, cmd);
        } finally {
            cmd.release();
            QMon.nettyRequestExecutorExecuteTimer(System.currentTimeMillis() - start);
        }
    }

    private void doExecute(final ChannelHandlerContext ctx, final RemotingCommand cmd) {
        final int opaque = cmd.getHeader().getOpaque();

        if (processor.rejectRequest()) {
            ctx.writeAndFlush(errorResp(CommandCode.BROKER_REJECT, cmd));
            return;
        }

        try {
            final CompletableFuture<Datagram> future = processor.processRequest(ctx, cmd);
            if (cmd.isOneWay()) {
                return;
            }

            if (future != null) {
                future.exceptionally(ex -> errorResp(CommandCode.BROKER_ERROR, cmd))
                        .thenAccept((datagram -> {
                            final RemotingHeader header = datagram.getHeader();
                            header.setOpaque(opaque);
                            header.setVersion(cmd.getHeader().getVersion());
                            header.setRequestCode(cmd.getHeader().getCode());
                            ctx.writeAndFlush(datagram);
                        }));
            }
        } catch (Throwable e) {
            LOG.error("doExecute request exception, channel:{}, cmd:{}", ctx.channel(), cmd, e);
            ctx.writeAndFlush(errorResp(CommandCode.BROKER_ERROR, cmd));
        }
    }

    private Datagram errorResp(final short code, final RemotingCommand command) {
        return RemotingBuilder.buildEmptyResponseDatagram(code, command.getHeader());
    }
}
