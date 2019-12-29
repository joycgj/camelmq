package camel.tc.cmq.store;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.atomic.AtomicLong;

// done
public class MaxPulledMessageSequence {

    private final String subject;
    private final String group;

    private final AtomicLong maxSequence;

    @JsonCreator
    public MaxPulledMessageSequence(@JsonProperty("subject") String subject,
                                    @JsonProperty("group") String group,
                                    @JsonProperty("maxSequence") long maxSequence) {
        this.subject = subject;
        this.group = group;
        this.maxSequence = new AtomicLong(maxSequence);
    }

    public String getSubject() {
        return subject;
    }

    public String getGroup() {
        return group;
    }

    public long getMaxSequence() {
        return maxSequence.get();
    }

    public void setMaxSequence(final long maxSequence) {
        this.maxSequence.set(maxSequence);
    }

    @Override
    public String toString() {
        return "MaxPulledMessageSequence{" +
                "subject='" + subject + '\'' +
                ", group='" + group + '\'' +
                ", maxSequence=" + maxSequence +
                '}';
    }
}
