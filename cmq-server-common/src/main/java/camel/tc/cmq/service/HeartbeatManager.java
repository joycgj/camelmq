package camel.tc.cmq.service;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import camel.tc.cmq.common.Disposable;
import camel.tc.cmq.concurrent.NamedThreadFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

// done
public class HeartbeatManager<T> implements Disposable {

    private final HashedWheelTimer timer;

    private final ConcurrentMap<T, Timeout> timeouts;

    public HeartbeatManager() {
        this.timeouts = new ConcurrentHashMap<>();
        this.timer = new HashedWheelTimer(new NamedThreadFactory("qmq-heartbeat"));
        this.timer.start();
    }

    public void cancel(T key) {
        Timeout timeout = timeouts.remove(key);
        if (timeout == null) return;

        timeout.cancel();
    }

    public void refreshHeartbeat(T key, TimerTask task, long timeout, TimeUnit unit) {
        Timeout context = timer.newTimeout(task, timeout, unit);
        final Timeout old = timeouts.put(key, context);
        if (old != null && !old.isCancelled() && !old.isExpired()) {
            old.cancel();
        }
    }

    @Override
    public void destroy() {
        timer.stop();
    }
}
