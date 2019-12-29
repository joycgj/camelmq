package camel.tc.cmq.meta;

import io.netty.buffer.ByteBuf;
import camel.tc.cmq.utils.PayloadHolderUtils;

// done
public class BrokerAcquireMetaRequestSerializer {

    public static void serialize(BrokerAcquireMetaRequest request, ByteBuf out) {
        PayloadHolderUtils.writeString(request.getHostname(), out);
        out.writeInt(request.getPort());
    }

    public static BrokerAcquireMetaRequest deSerialize(ByteBuf out) {
        BrokerAcquireMetaRequest request = new BrokerAcquireMetaRequest();
        request.setHostname(PayloadHolderUtils.readString(out));
        request.setPort(out.readInt());
        return request;
    }
}
