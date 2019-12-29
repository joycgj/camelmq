package camel.tc.cmq.store;

import java.nio.ByteBuffer;

// done
public class MessageLogRecord {

    private final String subject;
    private final long sequence;
    private final long wroteOffset;
    private final int wroteBytes;
    private final short headerSize;
    private final long baseOffset;
    private final ByteBuffer payload;
    private final LogSegment logSegment;

    public MessageLogRecord(String subject,
                            long sequence,
                            long wroteOffset,
                            int wroteBytes,
                            short headerSize,
                            long baseOffset,
                            ByteBuffer payload,
                            LogSegment logSegment) {
        this.subject = subject;
        this.sequence = sequence;
        this.wroteOffset = wroteOffset;
        this.wroteBytes = wroteBytes;
        this.headerSize = headerSize;
        this.baseOffset = baseOffset;
        this.payload = payload;
        this.logSegment = logSegment;
    }

    public String getSubject() {
        return subject;
    }

    public long getSequence() {
        return sequence;
    }

    public long getWroteOffset() {
        return wroteOffset;
    }

    public int getWroteBytes() {
        return wroteBytes;
    }

    public short getHeaderSize() {
        return headerSize;
    }

    public long getBaseOffset() {
        return baseOffset;
    }

    public ByteBuffer getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "MessageLogRecord{" +
                "subject='" + subject + '\'' +
                ", sequence=" + sequence +
                ", wroteOffset=" + wroteOffset +
                ", wroteBytes=" + wroteBytes +
                ", headerSize=" + headerSize +
                ", baseOffset=" + baseOffset +
                '}';
    }

    public LogSegment getLogSegment() {
        return logSegment;
    }
}
