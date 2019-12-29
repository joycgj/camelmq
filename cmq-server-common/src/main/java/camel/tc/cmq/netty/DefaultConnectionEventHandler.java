package camel.tc.cmq.netty;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import camel.tc.cmq.monitor.QMon;

import java.util.concurrent.atomic.AtomicLong;

// done
public class DefaultConnectionEventHandler implements ConnectionEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionHandler.class);

    private final String name;
    private final AtomicLong counter = new AtomicLong(0);

    public DefaultConnectionEventHandler(final String name) {
        this.name = name;
        QMon.activeConnectionGauge(name, counter::doubleValue);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LOG.info("[name: {}] client {} connected", name, ctx.channel().remoteAddress());
        counter.incrementAndGet();

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        LOG.info("[name: {}] client {} disconnected", name, ctx.channel().remoteAddress());
        counter.decrementAndGet();
    }
}
