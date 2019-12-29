package camel.tc.cmq.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import camel.tc.cmq.monitor.QMon;
import camel.tc.cmq.utils.RetrySubjectUtils;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 维护每个消费组的消费进度
 * nextSequence即下一次拉取的时候对应的consumer log上的sequence
 */
public class ConsumeQueue {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumeQueue.class);

    private final Storage storage;
    private final String subject;
    private final String group;
    private final AtomicLong nextSequence;
    private final AtomicBoolean monitorEnabled = new AtomicBoolean(false);

    public ConsumeQueue(final Storage storage, final String subject, final String group, final long lastMaxSequence) {
        this.storage = storage;
        this.subject = subject;
        this.group = group;
        this.nextSequence = new AtomicLong(lastMaxSequence);
    }

    public synchronized void setNextSequence(long nextSequence) {
        this.nextSequence.set(nextSequence);
    }

    public long getQueueCount() {
        return storage.getMaxMessageSequence(subject) - nextSequence.get();
    }

    public synchronized GetMessageResult pollMessages(final int maxMessages) {
        enableLagMonitor();

        long currentSequence = nextSequence.get();
        if (RetrySubjectUtils.isRetrySubject(subject)) {
            return storage.pollMessages(subject, currentSequence, maxMessages, this::isDelayReached);
        } else {
            final GetMessageResult result = storage.pollMessages(subject, currentSequence, maxMessages);
            long actualSequence = result.getNextBeginSequence() - result.getBuffers().size();
            long delta = actualSequence - currentSequence;
            if (delta > 0) {
                QMon.expiredMessagesCountInc(subject, group, delta);
                LOG.error("next sequence skipped. subject: {}, group: {}, nextSequence: {}, result: {}", subject, group, currentSequence, result);
            }
            return result;
        }
    }

    private boolean isDelayReached(MessageFilter.WithTimestamp entry) {
        final int delayMillis = storage.getStorageConfig().getRetryDelaySeconds() * 1000;
        return entry.getTimestamp() + delayMillis <= System.currentTimeMillis();
    }

    private void enableLagMonitor() {
        try {
            if (monitorEnabled.compareAndSet(false, true)) {
                QMon.messageSequenceLagGauge(subject, group, () -> (double) getQueueCount());
                LOG.info("enable message sequence lag monitor:{} {}", subject, group);
            }
        } catch (Throwable e) {
            LOG.error("enable message sequence lag monitor error:{} {}", subject, group, e);
        }
    }

    void disableLagMonitor(String subject, String group) {
        if (monitorEnabled.compareAndSet(true, false)) {
            // TODO: can we avoid remove this metrics by clean up all useless data after offline all consumers in this group?
            QMon.removeMessageSequenceLag(subject, group);
            LOG.info("disable message sequence lag monitor:{} {}", subject, group);
        }
    }
}
