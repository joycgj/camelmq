package camel.tc.cmq.store;

import com.google.common.collect.Table;
import camel.tc.cmq.base.RawMessage;
import camel.tc.cmq.common.Disposable;
import camel.tc.cmq.store.action.ActionEvent;
import camel.tc.cmq.store.buffer.SegmentBuffer;
import camel.tc.cmq.store.event.FixedExecOrderEventBus;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import java.util.Map;

// done
public interface Storage extends Disposable {

    void start();

    StorageConfig getStorageConfig();

    PutMessageResult appendMessage(final RawMessage message);

    SegmentBuffer getMessageData(final long wroteOffset);

    GetMessageResult getMessage(String subject, long sequence);

    GetMessageResult pollMessages(String subject, long startSequence, int maxMessages);

    GetMessageResult pollMessages(final String subject, final long startSequence, final int maxMessages, MessageFilter filter);

    long getMaxMessageOffset();

    long getMinMessageOffset();

    long getMaxActionLogOffset();

    long getMinActionLogOffset();

    long getMaxMessageSequence(final String subject);

    PutMessageResult putAction(final Action action);

    List<PutMessageResult> putPullLogs(final String subject, final String group, final String consumerId, final List<PullLogMessage> messages);

    CheckpointManager getCheckpointManager();

    ConsumerGroupProgress getConsumerGroupProgress(final String subject, final String group);

    Table<String, String, ConsumerGroupProgress> allConsumerGroupProgresses();

    long getMaxPulledMessageSequence(String subject, String group);

    long getMessageSequenceByPullLog(final String subject, final String group, final String consumerId, final long pullLogSequence);

    void updateConsumeQueue(String subject, String group, int consumeFromWhereCode);

    ConsumeQueue locateConsumeQueue(final String subject, final String group);

    Map<String, ConsumeQueue> locateSubjectConsumeQueues(final String subject);

    <T> void registerEventListener(final Class<T> clazz, final FixedExecOrderEventBus.Listener<T> listener);

    void registerActionEventListener(final FixedExecOrderEventBus.Listener<ActionEvent> listener);

    SegmentBuffer getActionLogData(final long offset);

    boolean appendMessageLogData(final long startOffset, final ByteBuffer data);

    boolean appendActionLogData(final long startOffset, final ByteBuffer data);

    MessageLogRecordVisitor newMessageLogVisitor(final long startOffset);

    void disableLagMonitor(String subject, String group);

    Table<String, String, PullLog> allPullLogs();

    void destroyPullLog(final String subject, final String group, final String consumerId);
}
