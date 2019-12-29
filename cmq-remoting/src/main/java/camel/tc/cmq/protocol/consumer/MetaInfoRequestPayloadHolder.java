package camel.tc.cmq.protocol.consumer;

import io.netty.buffer.ByteBuf;
import camel.tc.cmq.protocol.PayloadHolder;
import camel.tc.cmq.utils.PayloadHolderUtils;

// done
public class MetaInfoRequestPayloadHolder implements PayloadHolder {

    private final MetaInfoRequest request;

    public MetaInfoRequestPayloadHolder(MetaInfoRequest request) {
        this.request = request;
    }

    @Override
    public void writeBody(ByteBuf out) {
        PayloadHolderUtils.writeStringMap(request.getAttrs(), out);
    }
}
