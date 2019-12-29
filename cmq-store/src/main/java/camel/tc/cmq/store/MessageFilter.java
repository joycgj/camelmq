package camel.tc.cmq.store;

// done
interface MessageFilter {

    boolean filter(WithTimestamp entry);

    interface WithTimestamp {
        long getTimestamp();
    }
}
