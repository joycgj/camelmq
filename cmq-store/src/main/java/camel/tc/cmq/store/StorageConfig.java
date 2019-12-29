package camel.tc.cmq.store;

// done
public interface StorageConfig {

    String getCheckpointStorePath();

    String getMessageLogStorePath();

    long getMessageLogRetentionMs();

    String getConsumerLogStorePath();

    long getConsumerLogRetentionMs();

    int getLogRetentionCheckIntervalSeconds();

    String getPullLogStorePath();

    long getPullLogRetentionMs();

    String getActionLogStorePath();

    String getIndexLogStorePath();

    long getLogRetentionMs();

    String getSMTStorePath();

    long getSMTRetentionMs();

    int getRetryDelaySeconds();

    int getCheckpointRetainCount();

    long getActionCheckpointInterval();

    long getMessageCheckpointInterval();

    int getMaxReservedMemTable();

    int getMaxActiveMemTable();

    boolean isConsumerLogV2Enable();

    boolean isSMTEnable();

    long getLogDispatcherPauseMillis();
}
