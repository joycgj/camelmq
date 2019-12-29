package camel.tc.cmq.protocol.consumer;

// done
public class AckRequest {

    private static final byte UNSET = -1;
    private String subject;
    private String group;
    private String consumerId;
    private long pullOffsetBegin;
    private long pullOffsetLast;
    private byte isBroadcast = UNSET;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public long getPullOffsetBegin() {
        return pullOffsetBegin;
    }

    public void setPullOffsetBegin(long pullOffsetBegin) {
        this.pullOffsetBegin = pullOffsetBegin;
    }

    public long getPullOffsetLast() {
        return pullOffsetLast;
    }

    public void setPullOffsetLast(long pullOffsetLast) {
        this.pullOffsetLast = pullOffsetLast;
    }

    public void setBroadcast(byte isBroadcast) {
        this.isBroadcast = isBroadcast;
    }

    public byte isBroadcast() {
        return isBroadcast;
    }
}
