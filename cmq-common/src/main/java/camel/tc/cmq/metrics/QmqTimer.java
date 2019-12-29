package camel.tc.cmq.metrics;

import java.util.concurrent.TimeUnit;

// done
public interface QmqTimer {

    void update(long duration, TimeUnit unit);
}
