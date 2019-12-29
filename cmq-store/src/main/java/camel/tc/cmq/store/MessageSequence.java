package camel.tc.cmq.store;

// done
public class MessageSequence {

    private final long sequence;
    private final long physicalOffset;

    public MessageSequence(long sequence, long physicalOffset) {
        this.sequence = sequence;
        this.physicalOffset = physicalOffset;
    }

    public long getSequence() {
        return sequence;
    }

    public long getPhysicalOffset() {
        return physicalOffset;
    }
}
