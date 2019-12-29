package camel.tc.cmq.netty.client;

import com.google.common.util.concurrent.AbstractFuture;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import camel.tc.cmq.netty.DecodeHandler;
import camel.tc.cmq.netty.EncodeHandler;
import camel.tc.cmq.netty.NettyClientConfig;
import camel.tc.cmq.netty.exception.ClientSendException;
import camel.tc.cmq.netty.exception.RemoteTimeoutException;
import camel.tc.cmq.protocol.Datagram;
import camel.tc.cmq.util.RemoteHelper;

import java.util.concurrent.ExecutionException;

import static camel.tc.cmq.netty.exception.ClientSendException.SendErrorCode;

// done
public class NettyClient extends AbstractNettyClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);
    private static final NettyClient INSTANCE = new NettyClient();

    public static NettyClient getClient() {
        return INSTANCE;
    }

    private NettyClientHandler clientHandler;

    private NettyClient() {
        super("qmq-client");
    }

    @Override
    protected void initHandler() {
        clientHandler = new NettyClientHandler();
    }

    @Override
    protected void destroyHandler() {
        clientHandler.shutdown();
    }

    @Override
    protected ChannelInitializer<SocketChannel> newChannelInitializer(final NettyClientConfig config, final DefaultEventExecutorGroup eventExecutors, final NettyConnectManageHandler connectManager) {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(eventExecutors,
                        new EncodeHandler(),
                        new DecodeHandler(config.isServer()),
                        new IdleStateHandler(0, 0, config.getClientChannelMaxIdleTimeSeconds()),
                        connectManager,
                        clientHandler);
            }
        };
    }

    public Datagram sendSync(String brokerAddr, Datagram request, long responseTimeout) throws ClientSendException, InterruptedException, RemoteTimeoutException {
        ResultFuture result = new ResultFuture(brokerAddr);
        sendAsync(brokerAddr, request, responseTimeout, result);
        try {
            return result.get();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof RemoteTimeoutException) {
                throw (RemoteTimeoutException) e.getCause();
            }

            if (e.getCause() instanceof ClientSendException) {
                throw (ClientSendException) e.getCause();
            }

            throw new RuntimeException(e.getCause());
        }
    }

    private static final class ResultFuture extends AbstractFuture<Datagram> implements ResponseFuture.Callback {
        private final String brokerAddr;

        ResultFuture(String brokerAddr) {
            this.brokerAddr = brokerAddr;
        }

        @Override
        public void processResponse(ResponseFuture responseFuture) {
            if (!responseFuture.isSendOk()) {
                setException(new ClientSendException(ClientSendException.SendErrorCode.WRITE_CHANNEL_FAIL));
                return;
            }

            if (responseFuture.isTimeout()) {
                setException(new RemoteTimeoutException(brokerAddr, responseFuture.getTimeout()));
                return;
            }

            Datagram response = responseFuture.getResponse();
            if (response != null) {
                set(response);
            } else {
                setException(new ClientSendException(SendErrorCode.BROKER_BUSY));
            }
        }
    }

    public void sendAsync(String brokerAddr, Datagram request, long responseTimeout, ResponseFuture.Callback callback) throws ClientSendException {
        final Channel channel = getOrCreateChannel(brokerAddr);
        final ResponseFuture responseFuture = clientHandler.newResponse(channel, responseTimeout, callback);
        request.getHeader().setOpaque(responseFuture.getOpaque());

        try {
            channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) {
                    if (future.isSuccess()) {
                        responseFuture.setSendOk(true);
                        return;
                    }
                    clientHandler.removeResponse(channel, responseFuture);
                    responseFuture.completeBySendFail(future.cause());
                    LOGGER.error("send request to broker failed.", future.cause());
                    try {
                        responseFuture.executeCallbackOnlyOnce();
                    } catch (Throwable e) {
                        LOGGER.error("execute callback when send error exception", e);
                    }
                }
            });
        } catch (Exception e) {
            clientHandler.removeResponse(channel, responseFuture);
            responseFuture.completeBySendFail(e);
            LOGGER.warn("send request fail. brokerAddr={}", brokerAddr);
            throw new ClientSendException(SendErrorCode.WRITE_CHANNEL_FAIL, RemoteHelper.parseChannelRemoteAddress(channel), e);
        }
    }
}
