package camel.tc.cmq.metrics;

// done
public interface QmqMeter {

    void mark();

    void mark(long n);
}
