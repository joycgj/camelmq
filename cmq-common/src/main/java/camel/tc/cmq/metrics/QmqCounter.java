package camel.tc.cmq.metrics;

// done
public interface QmqCounter {

    void inc();

    void inc(long n);

    void dec();

    void dec(long n);
}
