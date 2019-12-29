package camel.tc.cmq.store;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

// done
public class ConsumeQueueManager {

    private final Table<String, String, ConsumeQueue> queues;
    private final Storage storage;

    public ConsumeQueueManager(final Storage storage) {
        this.queues = HashBasedTable.create();
        this.storage = storage;
    }

    public synchronized ConsumeQueue getOrCreate(final String subject, final String group) {
        if (!queues.contains(subject, group)) {
            final long nextSequence = getLastMaxSequence(subject, group).map(seq -> seq + 1).orElse(0L);
            queues.put(subject, group, new ConsumeQueue(storage, subject, group, nextSequence));
        }
        return queues.get(subject, group);
    }

    public synchronized Map<String, ConsumeQueue> getBySubject(final String subject) {
        if (queues.containsRow(subject)) {
            return queues.row(subject);
        } else {
            return Collections.emptyMap();
        }
    }

    private Optional<Long> getLastMaxSequence(final String subject, final String group) {
        final ConsumerGroupProgress progress = storage.getConsumerGroupProgress(subject, group);
        if (progress == null) {
            return Optional.empty();
        } else {
            return Optional.of(progress.getPull());
        }
    }

    public synchronized void update(final String subject, final String group, final long nextSequence) {
        final ConsumeQueue queue = getOrCreate(subject, group);
        queue.setNextSequence(nextSequence);
    }

    synchronized void disableLagMonitor(String subject, String group) {
        final ConsumeQueue consumeQueue = queues.get(subject, group);
        if (consumeQueue == null) {
            return;
        }
        consumeQueue.disableLagMonitor(subject, group);
    }
}
