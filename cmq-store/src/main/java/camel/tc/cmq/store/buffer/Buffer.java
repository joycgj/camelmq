package camel.tc.cmq.store.buffer;

import java.nio.ByteBuffer;

// done
public interface Buffer {

    ByteBuffer getBuffer();

    int getSize();

    boolean retain();

    boolean release();
}
