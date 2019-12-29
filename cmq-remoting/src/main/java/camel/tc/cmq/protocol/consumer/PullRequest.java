package camel.tc.cmq.protocol.consumer;

import java.util.List;

// done
public class PullRequest {

    private String subject;
    private String group;
    private int requestNum;
    private long timeoutMillis;
    private long offset;
    private long pullOffsetBegin;
    private long pullOffsetLast;
    private String consumerId;
    private boolean isBroadcast;
    private List<PullFilter> filters;

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

    public int getRequestNum() {
        return requestNum;
    }

    public void setRequestNum(int requestNum) {
        this.requestNum = requestNum;
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeoutMillis(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
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

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public boolean isBroadcast() {
        return isBroadcast;
    }

    public void setBroadcast(boolean broadcast) {
        isBroadcast = broadcast;
    }

    public List<PullFilter> getFilters() {
        return filters;
    }

    public void setFilters(final List<PullFilter> filters) {
        this.filters = filters;
    }

    @Override
    public String toString() {
        return "PullRequest{" +
                "subject='" + subject + '\'' +
                ", group='" + group + '\'' +
                ", requestNum=" + requestNum +
                ", timeoutMillis=" + timeoutMillis +
                ", offset=" + offset +
                ", pullOffsetBegin=" + pullOffsetBegin +
                ", pullOffsetLast=" + pullOffsetLast +
                ", consumerId='" + consumerId + '\'' +
                ", isBroadcast=" + isBroadcast +
                '}';
    }
}
