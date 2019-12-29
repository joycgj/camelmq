package camel.tc.cmq.meta;

import io.netty.buffer.ByteBuf;
import camel.tc.cmq.utils.PayloadHolderUtils;

public class BrokerRegisterRequestSerializer {

    public static void serialize(BrokerRegisterRequest request, ByteBuf out) {
        out.writeInt(request.getRequestType());
        PayloadHolderUtils.writeString(request.getGroupName(), out);
        PayloadHolderUtils.writeString(request.getBrokerAddress(), out);
        out.writeInt(request.getBrokerRole());
        out.writeInt(request.getBrokerState());
    }

    public static BrokerRegisterRequest deSerialize(ByteBuf out) {
        BrokerRegisterRequest request = new BrokerRegisterRequest();
        request.setRequestType(out.readInt());
        request.setGroupName(PayloadHolderUtils.readString(out));
        request.setBrokerAddress(PayloadHolderUtils.readString(out));
        request.setBrokerRole(out.readInt());
        request.setBrokerState(out.readInt());
        return request;
    }
}
