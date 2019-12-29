package camel.tc.cmq.store;

import java.util.Map;

// done
public class ConsumerGroupProgress {

    private final String subject;
    private final String group;
    private final Map<String, ConsumerProgress> consumers;
    // TODO: mark broadcast as final after new snapshot file created
    private boolean broadcast;
    private long pull;

    public ConsumerGroupProgress(String subject, String group, boolean broadcast, long pull, Map<String, ConsumerProgress> consumers) {
        this.subject = subject;
        this.group = group;
        this.broadcast = broadcast;
        this.pull = pull;
        this.consumers = consumers;
    }

    public String getSubject() {
        return subject;
    }

    public String getGroup() {
        return group;
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }

    public Map<String, ConsumerProgress> getConsumers() {
        return consumers;
    }

    public long getPull() {
        return pull;
    }

    public void setPull(long pull) {
        this.pull = pull;
    }
}
