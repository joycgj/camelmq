package camel.tc.cmq.store.event;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// done

/**
 * An event bus that execute listenersByType in fixed order
 */
public class FixedExecOrderEventBus {

    private final ReadWriteLock guard;
    private final ListMultimap<Class<?>, Listener> listenersByType;

    public FixedExecOrderEventBus() {
        this.guard = new ReentrantReadWriteLock();
        this.listenersByType = ArrayListMultimap.create();
    }

    public <E> void subscribe(final Class<E> clazz, final Listener<E> listener) {
        final Lock lock = guard.writeLock();
        lock.lock();
        try {
            listenersByType.put(clazz, listener);
        } finally {
            lock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public void post(final Object event) {
        final Lock lock = guard.readLock();

        lock.lock();

        try {
            final List<Listener> listeners = listenersByType.get(event.getClass());
            if (listeners == null) {
                throw new RuntimeException("unsupported event type " + event.getClass().getSimpleName());
            }

            for (final Listener listener : listeners) {
                listener.onEvent(event);
            }
        } finally {
            lock.unlock();
        }
    }

    public interface Listener<E> {
        void onEvent(final E event);
    }
}
