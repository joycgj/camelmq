package camel.tc.cmq.store.action;

import camel.tc.cmq.store.Action;
import camel.tc.cmq.store.ActionType;

public class RangeAckAction implements Action {

    private final String subject;
    private final String group;
    private final String consumerId;
    private final long timestamp;

    private final long firstSequence;
    private final long lastSequence;

    public RangeAckAction(String subject, String group, String consumerId, long timestamp, long firstSequence, long lastSequence) {
        this.subject = subject;
        this.group = group;
        this.consumerId = consumerId;
        this.timestamp = timestamp;

        this.firstSequence = firstSequence;
        this.lastSequence = lastSequence;
    }

    @Override
    public ActionType type() {
        return ActionType.RANGE_ACK;
    }

    @Override
    public String subject() {
        return subject;
    }

    @Override
    public String group() {
        return group;
    }

    @Override
    public String consumerId() {
        return consumerId;
    }

    @Override
    public long timestamp() {
        return timestamp;
    }

    public long getFirstSequence() {
        return firstSequence;
    }

    public long getLastSequence() {
        return lastSequence;
    }

    @Override
    public String toString() {
        return "RangeAckAction{" +
                "subject='" + subject + '\'' +
                ", group='" + group + '\'' +
                ", consumerId='" + consumerId + '\'' +
                ", firstSequence=" + firstSequence +
                ", lastSequence=" + lastSequence +
                '}';
    }
}
