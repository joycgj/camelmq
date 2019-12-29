package camel.tc.cmq.protocol.consumer;

import io.netty.buffer.ByteBuf;
import camel.tc.cmq.protocol.PayloadHolder;
import camel.tc.cmq.utils.PayloadHolderUtils;

// done
public class AckRequestPayloadHolder implements PayloadHolder {

    private final AckRequest request;

    public AckRequestPayloadHolder(AckRequest request) {
        this.request = request;
    }

    @Override
    public void writeBody(ByteBuf out) {
        PayloadHolderUtils.writeString(request.getSubject(), out);
        PayloadHolderUtils.writeString(request.getGroup(), out);
        PayloadHolderUtils.writeString(request.getConsumerId(), out);
        out.writeLong(request.getPullOffsetBegin());
        out.writeLong(request.getPullOffsetLast());
        out.writeByte(request.isBroadcast());
    }
}
