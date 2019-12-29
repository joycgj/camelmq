package camel.tc.cmq.protocol.consumer;

import io.netty.buffer.ByteBuf;
import camel.tc.cmq.protocol.PayloadHolder;

// done
public class PullRequestPayloadHolder implements PayloadHolder {

    private static final PullRequestSerde SERDE = new PullRequestSerde();

    private final PullRequest request;

    public PullRequestPayloadHolder(PullRequest request) {
        this.request = request;
    }

    @Override
    public void writeBody(ByteBuf out) {
        SERDE.write(request, out);
    }
}
