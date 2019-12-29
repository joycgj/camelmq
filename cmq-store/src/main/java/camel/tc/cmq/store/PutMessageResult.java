package camel.tc.cmq.store;

// done
public class PutMessageResult {

    private final PutMessageStatus status;
    private final AppendMessageResult<MessageSequence> result;

    public PutMessageResult(PutMessageStatus status, AppendMessageResult<MessageSequence> result) {
        this.status = status;
        this.result = result;
    }

    public PutMessageStatus getStatus() {
        return status;
    }

    public AppendMessageResult<MessageSequence> getResult() {
        return result;
    }
}
