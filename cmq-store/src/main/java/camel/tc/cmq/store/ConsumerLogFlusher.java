package camel.tc.cmq.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import camel.tc.cmq.concurrent.NamedThreadFactory;
import camel.tc.cmq.monitor.QMon;
import camel.tc.cmq.store.event.FixedExecOrderEventBus;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

// done
public class ConsumerLogFlusher implements FixedExecOrderEventBus.Listener<MessageLogRecord>, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerLogFlusher.class);

    private final StorageConfig config;
    private final CheckpointManager checkpointManager;
    private final ConsumerLogManager consumerLogManager;
    private final ScheduledExecutorService flushExecutor;
    private final AtomicLong counter;
    private volatile long latestFlushTime;

    public ConsumerLogFlusher(final StorageConfig config, final CheckpointManager checkpointManager, final ConsumerLogManager consumerLogManager) {
        this.config = config;
        this.checkpointManager = checkpointManager;
        this.consumerLogManager = consumerLogManager;
        this.flushExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("consumer-log-flusher"));
        this.counter = new AtomicLong(0);
        this.latestFlushTime = -1;

        scheduleForceFlushTask();
    }

    private void scheduleForceFlushTask() {
        flushExecutor.scheduleWithFixedDelay(this::tryForceSubmitFlushTask, 1, 1, TimeUnit.MINUTES);
    }

    private void tryForceSubmitFlushTask() {
        final long interval = System.currentTimeMillis() - latestFlushTime;
        if (interval < TimeUnit.MINUTES.toMillis(1)) {
            return;
        }

        submitFlushTask();
    }

    @Override
    public void onEvent(final MessageLogRecord event) {
        final long count = counter.incrementAndGet();
        if (count < config.getMessageCheckpointInterval()) {
            return;
        }

        QMon.consumerLogFlusherExceedCheckpointIntervalCountInc();
        submitFlushTask();
    }

    private synchronized void submitFlushTask() {
        counter.set(0);
        latestFlushTime = System.currentTimeMillis();

        final Snapshot<MessageCheckpoint> snapshot = checkpointManager.createMessageCheckpointSnapshot();
        flushExecutor.submit(() -> {
            final long start = System.currentTimeMillis();
            try {
                consumerLogManager.flush();
                checkpointManager.saveMessageCheckpointSnapshot(snapshot);
            } catch (Exception e) {
                QMon.consumerLogFlusherFlushFailedCountInc();
                LOG.error("flush consumer log failed. offset: {}", snapshot.getVersion(), e);
            } finally {
                QMon.consumerLogFlusherElapsedPerExecute(System.currentTimeMillis() - start);
            }
        });
    }

    @Override
    public void close() {
        LOG.info("try flush one more time before exit.");
        submitFlushTask();
        flushExecutor.shutdown();
        try {
            flushExecutor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            LOG.warn("interrupted during closing consumer log flusher.");
        }
    }
}
