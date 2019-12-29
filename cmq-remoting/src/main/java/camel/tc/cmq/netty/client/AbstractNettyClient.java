package camel.tc.cmq.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import camel.tc.cmq.netty.NettyClientConfig;
import camel.tc.cmq.netty.exception.ClientSendException;

import java.util.concurrent.atomic.AtomicBoolean;

// done
public abstract class AbstractNettyClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNettyClient.class);

    private final String clientName;
    private final AtomicBoolean started = new AtomicBoolean(false);
    private EventLoopGroup eventLoopGroup;
    private DefaultEventExecutorGroup eventExecutors;
    private NettyConnectManageHandler connectManager;

    protected AbstractNettyClient(String clientName) {
        this.clientName = clientName;
    }

    public boolean isStarted() {
        return started.get();
    }

    public synchronized void start(NettyClientConfig config) {
        if (started.get()) {
            return;
        }
        initHandler();
        Bootstrap bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(1, new DefaultThreadFactory(clientName + "-boss"));
        eventExecutors = new DefaultEventExecutorGroup(config.getClientWorkerThreads(), new DefaultThreadFactory(clientName + "-worker"));
        connectManager = new NettyConnectManageHandler(bootstrap, config.getConnectTimeoutMillis());
        bootstrap.group(this.eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnectTimeoutMillis())
                .option(ChannelOption.SO_SNDBUF, config.getClientSocketSndBufSize())
                .option(ChannelOption.SO_RCVBUF, config.getClientSocketRcvBufSize())
                .handler(newChannelInitializer(config, eventExecutors, connectManager));
        started.set(true);
    }

    public synchronized void shutdown() {
        if (!started.get()) {
            return;
        }
        try {
            connectManager.shutdown();
            eventLoopGroup.shutdownGracefully();
            eventExecutors.shutdownGracefully();
            destroyHandler();
            started.set(false);
        } catch (Exception e) {
            LOGGER.error("NettyClient {} shutdown exception, ", clientName, e);
        }
    }

    protected void initHandler() {
    }

    protected void destroyHandler() {
    }

    protected abstract ChannelInitializer<SocketChannel> newChannelInitializer(NettyClientConfig config, DefaultEventExecutorGroup eventExecutors, NettyConnectManageHandler connectManager);

    protected Channel getOrCreateChannel(String remoteAddr) throws ClientSendException {
        return connectManager.getOrCreateChannel(remoteAddr);
    }
}
