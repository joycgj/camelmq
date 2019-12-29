package camel.tc.cmq.configuration;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import camel.tc.cmq.meta.BrokerAcquireMetaResponse;
import camel.tc.cmq.meta.BrokerRole;
import camel.tc.cmq.utils.NetworkUtils;

// done
public final class BrokerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(BrokerConfig.class);

    private static final BrokerConfig CONFIG = new BrokerConfig();

    private volatile String brokerName;
    private volatile BrokerRole brokerRole;
    private volatile String brokerAddress;
    private volatile String masterAddress;
    private volatile boolean readonly;

    private BrokerConfig() {
        brokerName = "";
        brokerRole = BrokerRole.STANDBY;
        brokerAddress = NetworkUtils.getLocalAddress();
        masterAddress = "";
        readonly = true;
    }

    public static BrokerConfig getInstance() {
        return CONFIG;
    }

    public static String getBrokerName() {
        return CONFIG.brokerName;
    }

    public static BrokerRole getBrokerRole() {
        return CONFIG.brokerRole;
    }

    public static String getBrokerAddress() {
        return CONFIG.brokerAddress;
    }

    public static String getMasterAddress() {
        return CONFIG.masterAddress;
    }

    public static boolean isReadonly() {
        return CONFIG.readonly;
    }

    public static void markAsWritable() {
        CONFIG.readonly = false;
    }

    @Subscribe
    public void updateMeta(final BrokerAcquireMetaResponse response) {
        LOG.info("Broker meta updated. meta: {}", response);
        if (response.getRole() != BrokerRole.STANDBY) {
            brokerRole = response.getRole();
            brokerName = response.getName();
            masterAddress = response.getMaster();
        }
    }
}
