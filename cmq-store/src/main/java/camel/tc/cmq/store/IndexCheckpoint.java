package camel.tc.cmq.store;

// done
class IndexCheckpoint {

    private long msgOffset;
    private long indexOffset;

    IndexCheckpoint(long msgOffset, long indexOffset) {
        this.msgOffset = msgOffset;
        this.indexOffset = indexOffset;
    }

    long getMsgOffset() {
        return msgOffset;
    }

    void setMsgOffset(long msgOffset) {
        this.msgOffset = msgOffset;
    }

    long getIndexOffset() {
        return indexOffset;
    }

    void setIndexOffset(long indexOffset) {
        this.indexOffset = indexOffset;
    }
}
