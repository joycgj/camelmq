package camel.tc.cmq.store;

// done
public interface LogVisitor<Record> {

    LogVisitorRecord<Record> nextRecord();

    int visitedBufferSize();
}
