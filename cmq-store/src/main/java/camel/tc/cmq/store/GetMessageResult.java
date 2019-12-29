package camel.tc.cmq.store;

import camel.tc.cmq.store.buffer.Buffer;

import java.util.ArrayList;
import java.util.List;

// done
public class GetMessageResult {

    private final List<Buffer> buffers = new ArrayList<>(100);
    private int bufferTotalSize = 0;

    private GetMessageStatus status;
    private long nextBeginSequence;

    private OffsetRange consumerLogRange;

    public GetMessageResult() {
    }

    public GetMessageResult(GetMessageStatus status) {
        this.status = status;
    }

    public GetMessageStatus getStatus() {
        return status;
    }

    public void setStatus(GetMessageStatus status) {
        this.status = status;
    }

    public List<Buffer> getBuffers() {
        return buffers;
    }

    public void addBuffer(final Buffer buffer) {
        buffers.add(buffer);
        bufferTotalSize += buffer.getSize();
    }

    public int getMessageNum() {
        return buffers.size();
    }

    public long getNextBeginSequence() {
        return nextBeginSequence;
    }

    public void setNextBeginSequence(long nextBeginSequence) {
        this.nextBeginSequence = nextBeginSequence;
    }

    public int getBufferTotalSize() {
        return bufferTotalSize;
    }

    public OffsetRange getConsumerLogRange() {
        return consumerLogRange;
    }

    public void setConsumerLogRange(OffsetRange consumerLogRange) {
        this.consumerLogRange = consumerLogRange;
    }

    public void release() {
        for (Buffer buffer : buffers) {
            buffer.release();
        }
    }

    @Override
    public String toString() {
        return "GetMessageResult{" +
                "buffers=" + buffers.size() +
                ", bufferTotalSize=" + bufferTotalSize +
                ", status=" + status +
                ", nextBeginSequence=" + nextBeginSequence +
                ", consumerLogRange=" + consumerLogRange +
                '}';
    }
}
