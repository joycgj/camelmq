package camel.tc.cmq.store.buffer;

import qunar.tc.qmq.store.LogSegment;

import java.nio.ByteBuffer;

// done
public class SegmentBuffer implements Buffer {

    private final long startOffset;
    private final int size;

    private final ByteBuffer buffer;
    private final LogSegment logSegment;

    public SegmentBuffer(long startOffset, ByteBuffer buffer, int size, LogSegment logSegment) {
        this.startOffset = startOffset;
        this.size = size;
        this.buffer = buffer;
        this.logSegment = logSegment;
    }

    public long getStartOffset() {
        return startOffset;
    }

    @Override
    public ByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public int getSize() {
        return size;
    }

    public LogSegment getLogSegment() {
        return logSegment;
    }

    @Override
    public boolean release() {
        return logSegment.release();
    }

    @Override
    public boolean retain() {
        return logSegment.retain();
    }
}
