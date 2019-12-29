package camel.tc.cmq.store;

// done
class OffsetBound {

    private final long minOffset;
    private final long maxOffset;

    OffsetBound(long minOffset, long maxOffset) {
        this.minOffset = minOffset;
        this.maxOffset = maxOffset;
    }

    public long getMinOffset() {
        return minOffset;
    }

    public long getMaxOffset() {
        return maxOffset;
    }
}
