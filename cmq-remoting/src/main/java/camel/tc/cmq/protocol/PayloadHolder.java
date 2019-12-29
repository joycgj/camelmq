package camel.tc.cmq.protocol;

import io.netty.buffer.ByteBuf;

// done
public interface PayloadHolder {

    void writeBody(ByteBuf out);
}
