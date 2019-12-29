package camel.tc.cmq.store;

// done
public class OffsetRange {
    private final long begin;
    private final long end;

    public OffsetRange(long begin, long end) {
        this.begin = begin;
        this.end = end;
    }

    public long getBegin() {
        return begin;
    }

    public long getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "OffsetRange{" +
                "begin=" + begin +
                ", end=" + end +
                '}';
    }
}
