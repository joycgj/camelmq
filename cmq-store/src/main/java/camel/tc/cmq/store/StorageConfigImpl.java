package camel.tc.cmq.store;

import camel.tc.cmq.configuration.DynamicConfig;
import camel.tc.cmq.constants.BrokerConstants;

import java.io.File;
import java.util.concurrent.TimeUnit;

// done
public class StorageConfigImpl implements StorageConfig {

    private static final String CHECKPOINT = "checkpoint";
    private static final String MESSAGE_LOG = "messagelog";
    private static final String CONSUMER_LOG = "consumerlog";
    private static final String PULL_LOG = "pulllog";
    private static final String ACTION_LOG = "actionlog";
    private static final String INDEX_LOG = "indexlog";
    private static final String SMT = "smt";

    private static final long MS_PER_HOUR = TimeUnit.HOURS.toMillis(1);

    private final DynamicConfig config;

    public StorageConfigImpl(final DynamicConfig config) {
        this.config = config;
    }

    @Override
    public String getCheckpointStorePath() {
        return buildStorePath(CHECKPOINT);
    }

    @Override
    public String getMessageLogStorePath() {
        return buildStorePath(MESSAGE_LOG);
    }

    @Override
    public long getMessageLogRetentionMs() {
        final int retentionHours = config.getInt(BrokerConstants.MESSAGE_LOG_RETENTION_HOURS, BrokerConstants.DEFAULT_MESSAGE_LOG_RETENTION_HOURS);
        return retentionHours * MS_PER_HOUR;
    }

    @Override
    public String getConsumerLogStorePath() {
        return buildStorePath(CONSUMER_LOG);
    }

    @Override
    public long getConsumerLogRetentionMs() {
        final int retentionHours = config.getInt(BrokerConstants.CONSUMER_LOG_RETENTION_HOURS, BrokerConstants.DEFAULT_CONSUMER_LOG_RETENTION_HOURS);
        return retentionHours * MS_PER_HOUR;
    }

    @Override
    public int getLogRetentionCheckIntervalSeconds() {
        return config.getInt(BrokerConstants.LOG_RETENTION_CHECK_INTERVAL_SECONDS, BrokerConstants.DEFAULT_LOG_RETENTION_CHECK_INTERVAL_SECONDS);
    }

    @Override
    public String getPullLogStorePath() {
        return buildStorePath(PULL_LOG);
    }

    @Override
    public long getPullLogRetentionMs() {
        final int retentionHours = config.getInt(BrokerConstants.PULL_LOG_RETENTION_HOURS, BrokerConstants.DEFAULT_PULL_LOG_RETENTION_HOURS);
        return retentionHours * MS_PER_HOUR;
    }

    @Override
    public String getActionLogStorePath() {
        return buildStorePath(ACTION_LOG);
    }

    @Override
    public String getIndexLogStorePath() {
        return buildStorePath(INDEX_LOG);
    }

    private String buildStorePath(final String name) {
        final String root = config.getString(BrokerConstants.STORE_ROOT, BrokerConstants.LOG_STORE_ROOT);
        return new File(root, name).getAbsolutePath();
    }

    @Override
    public long getLogRetentionMs() {
        final int retentionHours = config.getInt(BrokerConstants.PULL_LOG_RETENTION_HOURS, BrokerConstants.DEFAULT_PULL_LOG_RETENTION_HOURS);
        return retentionHours * MS_PER_HOUR;
    }

    @Override
    public String getSMTStorePath() {
        return buildStorePath(SMT);
    }

    @Override
    public long getSMTRetentionMs() {
        final int retentionHours = config.getInt(BrokerConstants.SMT_RETENTION_HOURS, BrokerConstants.DEFAULT_SMT_RETENTION_HOURS);
        return retentionHours * MS_PER_HOUR;
    }

    @Override
    public int getRetryDelaySeconds() {
        return config.getInt(BrokerConstants.RETRY_DELAY_SECONDS, BrokerConstants.DEFAULT_RETRY_DELAY_SECONDS);
    }

    @Override
    public int getCheckpointRetainCount() {
        return config.getInt(BrokerConstants.CHECKPOINT_RETAIN_COUNT, BrokerConstants.DEFAULT_CHECKPOINT_RETAIN_COUNT);
    }

    @Override
    public long getActionCheckpointInterval() {
        return config.getLong(BrokerConstants.ACTION_CHECKPOINT_INTERVAL, BrokerConstants.DEFAULT_ACTION_CHECKPOINT_INTERVAL);
    }

    @Override
    public long getMessageCheckpointInterval() {
        return config.getLong(BrokerConstants.MESSAGE_CHECKPOINT_INTERVAL, BrokerConstants.DEFAULT_MESSAGE_CHECKPOINT_INTERVAL);
    }

    @Override
    public int getMaxReservedMemTable() {
        return config.getInt(BrokerConstants.MAX_RESERVED_MEMTABLE, BrokerConstants.DEFAULT_MAX_RESERVED_MEMTABLE);
    }

    @Override
    public int getMaxActiveMemTable() {
        return config.getInt(BrokerConstants.MAX_ACTIVE_MEMTABLE, BrokerConstants.DEFAULT_MAX_ACTIVE_MEMTABLE);
    }

    @Override
    public boolean isConsumerLogV2Enable() {
        return config.getBoolean(BrokerConstants.CONSUMER_LOG_V2_ENABLE, false);
    }

    @Override
    public boolean isSMTEnable() {
        if (!isConsumerLogV2Enable()) {
            return false;
        }

        return config.getBoolean(BrokerConstants.SMT_ENABLE, false);
    }

    @Override
    public long getLogDispatcherPauseMillis() {
        return config.getLong(BrokerConstants.LOG_DISPATCHER_PAUSE_MILLIS, 5);
    }
}
