package camel.tc.cmq.store;

import java.util.Map;

// done
public class MessageCheckpoint {

    private final Map<String, Long> maxSequences;
    private long offset;

    public MessageCheckpoint(long offset, Map<String, Long> maxSequences) {
        this.offset = offset;
        this.maxSequences = maxSequences;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public Map<String, Long> getMaxSequences() {
        return maxSequences;
    }
}
