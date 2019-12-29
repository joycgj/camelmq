package camel.tc.cmq.meta;

import io.netty.buffer.ByteBuf;
import camel.tc.cmq.utils.PayloadHolderUtils;

public class BrokerAcquireMetaResponseSerializer {

    public static void serialize(BrokerAcquireMetaResponse response, ByteBuf out) {
        PayloadHolderUtils.writeString(response.getName(), out);
        PayloadHolderUtils.writeString(response.getMaster(), out);
        out.writeInt(response.getRole().getCode());
    }

    public static BrokerAcquireMetaResponse deSerialize(ByteBuf out) {
        BrokerAcquireMetaResponse response = new BrokerAcquireMetaResponse();
        response.setName(PayloadHolderUtils.readString(out));
        response.setMaster(PayloadHolderUtils.readString(out));
        response.setRole(BrokerRole.fromCode(out.readInt()));
        return response;
    }
}
