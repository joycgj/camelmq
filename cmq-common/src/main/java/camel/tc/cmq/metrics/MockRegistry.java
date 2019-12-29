package camel.tc.cmq.metrics;

import com.google.common.base.Supplier;

import java.util.concurrent.TimeUnit;

// done
class MockRegistry implements QmqMetricRegistry {

    private static final QmqCounter COUNTER = new MockCounter();

    private static final QmqMeter METER = new MockMeter();

    private static final QmqTimer TIMER = new MockTimer();

    @Override
    public void newGauge(String name, String[] tags, String[] values, Supplier<Double> supplier) {
    }

    @Override
    public QmqCounter newCounter(String name, String[] tags, String[] values) {
        return COUNTER;
    }

    @Override
    public QmqMeter newMeter(String name, String[] tags, String[] values) {
        return METER;
    }

    @Override
    public QmqTimer newTimer(String name, String[] tags, String[] values) {
        return TIMER;
    }

    @Override
    public void remove(String name, String[] tags, String[] values) {
    }

    private static class MockCounter implements QmqCounter {

        @Override
        public void inc() {
        }

        @Override
        public void inc(long n) {
        }

        @Override
        public void dec() {
        }

        @Override
        public void dec(long n) {
        }
    }

    private static class MockMeter implements QmqMeter {

        @Override
        public void mark() {
        }

        @Override
        public void mark(long n) {
        }
    }

    private static class MockTimer implements QmqTimer {

        @Override
        public void update(long duration, TimeUnit unit) {
        }
    }
}
