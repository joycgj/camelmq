package camel.tc.cmq.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import camel.tc.cmq.monitor.QMon;
import camel.tc.cmq.store.event.FixedExecOrderEventBus;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

// done
public class LogIterateService<T> implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(LogIterateService.class);

    private final String name;
    private final long dispatcherPauseMills;
    private final Visitable<T> visitable;
    private final FixedExecOrderEventBus dispatcher;
    private final Thread dispatcherThread;

    private final LongAdder iterateFrom;
    private volatile boolean stop = false;

    public LogIterateService(final String name, final long dispatcherPauseMills, final Visitable<T> visitable, final long checkpoint, final FixedExecOrderEventBus dispatcher) {
        this.name = name;
        this.dispatcherPauseMills = dispatcherPauseMills;
        this.visitable = visitable;
        this.dispatcher = dispatcher;
        this.dispatcherThread = new Thread(new Dispatcher());
        this.dispatcherThread.setName(name);
        this.iterateFrom = new LongAdder();
        this.iterateFrom.add(initialMessageIterateFrom(visitable, checkpoint));

        QMon.replayLag(name + "Lag", () -> (double) replayLogLag());
    }

    private long initialMessageIterateFrom(final Visitable<T> log, final long checkpoint) {
        if (checkpoint <= 0) {
            return log.getMinOffset();
        }
        if (checkpoint > log.getMaxOffset()) {
            return log.getMaxOffset();
        }
        return checkpoint;
    }

    public void start() {
        dispatcherThread.start();
    }

    public void blockUntilReplayDone() {
        LOG.info("replay log initial lag: {}; min: {}, max: {}, from: {}",
                replayLogLag(), visitable.getMinOffset(), visitable.getMaxOffset(), iterateFrom.longValue());

        while (replayLogLag() > 0) {
            LOG.info("waiting replay log ...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                LOG.warn("block until replay done interrupted", e);
            }
        }
    }

    private long replayLogLag() {
        return visitable.getMaxOffset() - iterateFrom.longValue();
    }

    @Override
    public void close() {
        stop = true;
        try {
            dispatcherThread.join();
        } catch (InterruptedException e) {
            LOG.error("log dispatcher thread interrupted", e);
        }
    }

    private class Dispatcher implements Runnable {
        @Override
        public void run() {
            while (!stop) {
                try {
                    processLog();
                } catch (Throwable e) {
                    QMon.replayLogFailedCountInc(name + "Failed");
                    LOG.error("replay log failed, will retry.", e);
                }
            }
        }

        private void processLog() {
            final long startOffset = iterateFrom.longValue();
            try (AbstractLogVisitor<T> visitor = visitable.newVisitor(startOffset)) {
                if (startOffset != visitor.getStartOffset()) {
                    LOG.info("reset iterate from offset from {} to {}", startOffset, visitor.getStartOffset());
                    iterateFrom.reset();
                    iterateFrom.add(visitor.getStartOffset());
                }

                while (true) {
                    final LogVisitorRecord<T> record = visitor.nextRecord();
                    if (record.isNoMore()) {
                        break;
                    }

                    if (record.hasData()) {
                        dispatcher.post(record.getData());
                    }
                }
                iterateFrom.add(visitor.visitedBufferSize());
            }

            try {
                TimeUnit.MILLISECONDS.sleep(dispatcherPauseMills);
            } catch (InterruptedException e) {
                LOG.warn("log dispatcher sleep interrupted");
            }
        }
    }
}