package camel.tc.cmq.store.action;

import com.google.common.base.Preconditions;
import camel.tc.cmq.store.Action;
import camel.tc.cmq.store.ActionType;

// done
public class PullAction implements Action {
    
    private final String subject;
    private final String group;
    private final String consumerId;
    private final long timestamp;
    private final boolean broadcast;

    //first sequence of pull log
    private final long firstSequence;

    //last sequence of pull log
    private final long lastSequence;

    //fist sequence of consumer log
    private final long firstMessageSequence;

    //last sequence of consumer log
    private final long lastMessageSequence;

    public PullAction(final String subject, final String group, final String consumerId, long timestamp, boolean broadcast,
                      long firstSequence, long lastSequence,
                      long firstMessageSequence, long lastMessageSequence) {
        Preconditions.checkArgument(lastSequence - firstSequence == lastMessageSequence - firstMessageSequence);

        this.subject = subject;
        this.group = group;
        this.consumerId = consumerId;
        this.timestamp = timestamp;
        this.broadcast = broadcast;

        this.firstSequence = firstSequence;
        this.lastSequence = lastSequence;

        this.firstMessageSequence = firstMessageSequence;
        this.lastMessageSequence = lastMessageSequence;
    }

    @Override
    public ActionType type() {
        return ActionType.PULL;
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

    public boolean isBroadcast() {
        return broadcast;
    }

    /**
     * 在pull log中第一个偏移
     */
    public long getFirstSequence() {
        return firstSequence;
    }

    /**
     * 在pull log中最后一个偏移
     */
    public long getLastSequence() {
        return lastSequence;
    }

    /**
     * 在consuemr log中第一个偏移
     */
    public long getFirstMessageSequence() {
        return firstMessageSequence;
    }

    /**
     * 在consumer log中最后一个偏移
     */
    public long getLastMessageSequence() {
        return lastMessageSequence;
    }

    @Override
    public String toString() {
        return "PullAction{" +
                "subject='" + subject + '\'' +
                ", group='" + group + '\'' +
                ", consumerId='" + consumerId + '\'' +
                ", broadcast=" + broadcast +
                ", firstSequence=" + firstSequence +
                ", lastSequence=" + lastSequence +
                ", firstMessageSequence=" + firstMessageSequence +
                ", lastMessageSequence=" + lastMessageSequence +
                '}';
    }
}
