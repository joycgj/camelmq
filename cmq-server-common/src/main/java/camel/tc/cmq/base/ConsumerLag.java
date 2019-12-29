package camel.tc.cmq.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// done
public class ConsumerLag {
    private final long pull;
    private final long ack;

    @JsonCreator
    public ConsumerLag(@JsonProperty("pull") final long pull, @JsonProperty("ack") final long ack) {
        this.pull = pull;
        this.ack = ack;
    }

    public long getPull() {
        return pull;
    }

    public long getAck() {
        return ack;
    }
}
