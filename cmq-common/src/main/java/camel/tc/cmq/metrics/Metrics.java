package camel.tc.cmq.metrics;

import com.google.common.base.Supplier;

import java.util.ServiceLoader;

// done
public class Metrics {

    private static final String[] EMPTY = new String[0];

    private static final QmqMetricRegistry INSTANCE;

    static {
        ServiceLoader<QmqMetricRegistry> registries = ServiceLoader.load(QmqMetricRegistry.class);
        QmqMetricRegistry instance = null;
        for (QmqMetricRegistry registry : registries) {
            instance = registry;
            break;
        }
        if (instance == null) {
            instance = new MockRegistry();
        }

        INSTANCE = instance;
    }

    public static void gauge(String name, String[] tags, String[] values, Supplier<Double> supplier) {
        INSTANCE.newGauge(name, tags, values, supplier);
    }

    public static void gauge(String name, Supplier<Double> supplier) {
        INSTANCE.newGauge(name, EMPTY, EMPTY, supplier);
    }

    public static QmqCounter counter(String name, String[] tags, String[] values) {
        return INSTANCE.newCounter(name, tags, values);
    }

    public static QmqCounter counter(String name) {
        return INSTANCE.newCounter(name, EMPTY, EMPTY);
    }

    public static QmqMeter meter(String name, String[] tags, String[] values) {
        return INSTANCE.newMeter(name, tags, values);
    }

    public static QmqMeter meter(String name) {
        return INSTANCE.newMeter(name, EMPTY, EMPTY);
    }

    public static QmqTimer timer(String name, String[] tags, String[] values) {
        return INSTANCE.newTimer(name, tags, values);
    }

    public static QmqTimer timer(String name) {
        return INSTANCE.newTimer(name, EMPTY, EMPTY);
    }

    public static void remove(String name, String[] tags, String[] values) {
        INSTANCE.remove(name, tags, values);
    }
}