package camel.tc.cmq.store;

// done
public interface Serde<V> {

    byte[] toBytes(final V value);

    V fromBytes(final byte[] data);
}
