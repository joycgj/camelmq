package camel.tc.cmq.store;

// done
public class Snapshot<T> {

    private final long version;
    private final T data;

    public Snapshot(final long version, final T data) {
        this.version = version;
        this.data = data;
    }

    public long getVersion() {
        return version;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Snapshot{" +
                "version=" + version +
                '}';
    }
}
