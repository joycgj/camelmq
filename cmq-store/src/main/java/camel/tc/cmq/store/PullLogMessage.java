package camel.tc.cmq.store;

// done
public class PullLogMessage {
    private final long sequence;
    private final long messageSequence;

    public PullLogMessage(final long sequence, final long messageSequence) {
        this.sequence = sequence;
        this.messageSequence = messageSequence;
    }

    public long getSequence() {
        return sequence;
    }

    public long getMessageSequence() {
        return messageSequence;
    }

    @Override
    public String toString() {
        return "PullLogMessage{" +
                "sequence=" + sequence +
                ", messageSequence=" + messageSequence +
                '}';
    }
}
