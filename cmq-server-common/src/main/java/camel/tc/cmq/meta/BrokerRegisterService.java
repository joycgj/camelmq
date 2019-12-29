package camel.tc.cmq.meta;

import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import camel.tc.cmq.common.Disposable;
import camel.tc.cmq.concurrent.NamedThreadFactory;
import camel.tc.cmq.configuration.BrokerConfig;
import camel.tc.cmq.netty.NettyClientConfig;
import camel.tc.cmq.netty.client.NettyClient;
import camel.tc.cmq.protocol.CommandCode;
import camel.tc.cmq.protocol.Datagram;
import camel.tc.cmq.protocol.RemotingHeader;
import camel.tc.cmq.utils.NetworkUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// done
public class BrokerRegisterService implements Disposable {

    private static final Logger LOG = LoggerFactory.getLogger(BrokerRegisterService.class);

    private static final long TIMEOUT_MS = TimeUnit.SECONDS.toMillis(2);
    private static final int HEARTBEAT_DELAY_SECONDS = 10;

    private final ScheduledExecutorService heartbeatScheduler;
    private final MetaServerLocator locator;
    private final NettyClient client;
    private final int port;
    private final String brokerAddress;

    private volatile int brokerState;
    private volatile String endpoint;

    public BrokerRegisterService(final int port, final MetaServerLocator locator) {
        this.heartbeatScheduler = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("broker-register-heartbeat"));
        this.locator = locator;
        this.client = NettyClient.getClient();
        NettyClientConfig config = new NettyClientConfig();
        config.setClientChannelMaxIdleTimeSeconds(HEARTBEAT_DELAY_SECONDS * 2);
        config.setClientWorkerThreads(1);
        this.client.start(config);
        this.port = port;
        this.brokerAddress = BrokerConfig.getBrokerAddress() + ":" + port;
        this.brokerState = BrokerState.NRW.getCode();

        repickEndpoint();
    }

    public void start() {
        acquireMeta();
        heartbeatScheduler.scheduleWithFixedDelay(this::heartbeat, 0, HEARTBEAT_DELAY_SECONDS, TimeUnit.SECONDS);
    }

    private void acquireMeta() {
        Datagram datagram = null;
        try {
            datagram = client.sendSync(endpoint, buildAcquireMetaDatagram(), TIMEOUT_MS);
            final BrokerAcquireMetaResponse meta = BrokerAcquireMetaResponseSerializer.deSerialize(datagram.getBody());
            BrokerConfig.getInstance().updateMeta(meta);
        } catch (Exception e) {
            LOG.error("Send acquire meta message to meta server failed", e);
            throw new RuntimeException(e);
        } finally {
            if (datagram != null) {
                datagram.release();
            }
        }
    }

    private void heartbeat() {
        try {
            Datagram datagram = null;
            try {
                datagram = client.sendSync(endpoint, buildRegisterDatagram(BrokerRequestType.HEARTBEAT), TIMEOUT_MS);
            } catch (Exception e) {
                LOG.error("Send HEARTBEAT message to meta server failed", e);
                repickEndpoint();
            } finally {
                if (datagram != null) {
                    datagram.release();
                }
            }
        } catch (Exception e) {
            LOG.error("Heartbeat error", e);
        }
    }

    public void healthSwitch(final Boolean online) {
        if (online) {
            brokerOnline();
        } else {
            brokerOffline();
        }
    }

    private void brokerOnline() {
        Datagram datagram = null;
        try {
            brokerState = BrokerState.RW.getCode();
            datagram = client.sendSync(endpoint, buildRegisterDatagram(BrokerRequestType.ONLINE), TIMEOUT_MS);
        } catch (Exception e) {
            LOG.error("Send ONLINE message to meta server failed", e);
            repickEndpoint();
            throw new RuntimeException("broker online failed", e);
        } finally {
            if (datagram != null) {
                datagram.release();
            }
        }
    }

    private void brokerOffline() {
        Datagram datagram = null;
        try {
            brokerState = BrokerState.NRW.getCode();
            datagram = client.sendSync(endpoint, buildRegisterDatagram(BrokerRequestType.OFFLINE), TIMEOUT_MS);
        } catch (Exception e) {
            LOG.error("Send OFFLINE message to meta server failed", e);
            repickEndpoint();
            throw new RuntimeException("broker offline failed", e);
        } finally {
            if (datagram != null) {
                datagram.release();
            }
        }
    }

    private void repickEndpoint() {
        Optional<String> optional = locator.queryEndpoint();
        if (optional.isPresent()) {
            this.endpoint = optional.get();
        }
    }

    private Datagram buildAcquireMetaDatagram() {
        final Datagram datagram = new Datagram();
        final RemotingHeader header = new RemotingHeader();
        header.setCode(CommandCode.BROKER_ACQUIRE_META);
        datagram.setHeader(header);
        datagram.setPayloadHolder(out -> {
            final BrokerAcquireMetaRequest request = new BrokerAcquireMetaRequest();
            request.setHostname(NetworkUtils.getLocalHostname());
            request.setPort(port);
            BrokerAcquireMetaRequestSerializer.serialize(request, out);
        });
        return datagram;
    }

    private Datagram buildRegisterDatagram(final BrokerRequestType checkType) {
        final Datagram datagram = new Datagram();
        final RemotingHeader header = new RemotingHeader();
        header.setCode(CommandCode.BROKER_REGISTER);
        datagram.setHeader(header);
        datagram.setPayloadHolder(out -> {
            final BrokerRegisterRequest request = buildRegisterRequest(checkType);
            BrokerRegisterRequestSerializer.serialize(request, out);
        });
        return datagram;
    }

    private BrokerRegisterRequest buildRegisterRequest(final BrokerRequestType checkType) {
        final BrokerRegisterRequest request = new BrokerRegisterRequest();
        request.setGroupName(BrokerConfig.getBrokerName());
        request.setBrokerRole(BrokerConfig.getBrokerRole().getCode());
        request.setBrokerState(brokerState);
        request.setRequestType(checkType.getCode());
        request.setBrokerAddress(brokerAddress);
        return request;
    }

    @Override
    public void destroy() {
        heartbeatScheduler.shutdown();
        try {
            heartbeatScheduler.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.error("Shutdown heartbeat scheduler interrupted.");
        }
    }
}
