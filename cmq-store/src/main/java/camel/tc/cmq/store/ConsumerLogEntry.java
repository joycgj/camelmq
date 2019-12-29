package camel.tc.cmq.store;

// done
public class ConsumerLogEntry implements MessageFilter.WithTimestamp {

    private long timestamp;
    private long wroteOffset;
    private int wroteBytes;
    private short headerSize;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getWroteOffset() {
        return wroteOffset;
    }

    public void setWroteOffset(long wroteOffset) {
        this.wroteOffset = wroteOffset;
    }

    public int getWroteBytes() {
        return wroteBytes;
    }

    public void setWroteBytes(int wroteBytes) {
        this.wroteBytes = wroteBytes;
    }

    public short getHeaderSize() {
        return headerSize;
    }

    public void setHeaderSize(short headerSize) {
        this.headerSize = headerSize;
    }

    public static class Factory {
        private static final ThreadLocal<ConsumerLogEntry> ENTRY = ThreadLocal.withInitial(ConsumerLogEntry::new);

        public static ConsumerLogEntry create() {
            return ENTRY.get();
        }
    }
}
