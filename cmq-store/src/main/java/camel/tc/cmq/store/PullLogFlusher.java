package camel.tc.cmq.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import camel.tc.cmq.concurrent.NamedThreadFactory;
import camel.tc.cmq.monitor.QMon;
import camel.tc.cmq.store.action.ActionEvent;
import camel.tc.cmq.store.event.FixedExecOrderEventBus;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

// done
public class PullLogFlusher implements FixedExecOrderEventBus.Listener<ActionEvent>, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(PullLogFlusher.class);

    private final StorageConfig config;
    private final CheckpointManager checkpointManager;
    private final PullLogManager pullLogManager;
    private final ScheduledExecutorService flushExecutor;
    private final AtomicLong counter;
    private volatile long latestFlushTime;

    public PullLogFlusher(final StorageConfig config, final CheckpointManager checkpointManager, final PullLogManager pullLogManager) {
        this.config = config;
        this.checkpointManager = checkpointManager;
        this.pullLogManager = pullLogManager;
        this.flushExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("pull-log-flusher"));
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
    public void onEvent(ActionEvent event) {
        final long count = counter.incrementAndGet();
        if (count < config.getActionCheckpointInterval()) {
            return;
        }

        QMon.pullLogFlusherExceedCheckpointIntervalCountInc();
        submitFlushTask();
    }

    private synchronized void submitFlushTask() {
        counter.set(0);
        latestFlushTime = System.currentTimeMillis();

        final Snapshot<ActionCheckpoint> snapshot = checkpointManager.createActionCheckpointSnapshot();
        flushExecutor.submit(() -> {
            final long start = System.currentTimeMillis();
            try {
                pullLogManager.flush();
                checkpointManager.saveActionCheckpointSnapshot(snapshot);
            } catch (Exception e) {
                QMon.pullLogFlusherFlushFailedCountInc();
                LOG.error("flush pull log failed.", e);
            } finally {
                QMon.pullLogFlusherElapsedPerExecute(System.currentTimeMillis() - start);
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
            LOG.warn("interrupted during closing pull log flusher.");
        }
    }
}
