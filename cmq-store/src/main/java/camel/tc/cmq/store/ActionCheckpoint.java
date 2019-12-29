package camel.tc.cmq.store;

import com.google.common.collect.Table;

// done
public class ActionCheckpoint {

    private final Table<String, String, ConsumerGroupProgress> progresses;
    private long offset;

    public ActionCheckpoint(long offset, Table<String, String, ConsumerGroupProgress> progresses) {
        this.offset = offset;
        this.progresses = progresses;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public Table<String, String, ConsumerGroupProgress> getProgresses() {
        return progresses;
    }
}
