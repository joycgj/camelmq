package camel.tc.cmq.metrics;

import com.google.common.base.Supplier;

// done
public interface QmqMetricRegistry {

    void newGauge(final String name, final String[] tags, final String[] values, final Supplier<Double> supplier);

    QmqCounter newCounter(final String name, final String[] tags, final String[] values);

    QmqMeter newMeter(final String name, final String[] tags, final String[] values);

    QmqTimer newTimer(final String name, final String[] tags, final String[] values);

    void remove(final String name, final String[] tags, final String[] values);
}
