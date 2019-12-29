package camel.tc.cmq.store;

import io.netty.buffer.ByteBuf;

// done
public interface CheckpointLoader {

    ByteBuf loadCheckpoint();
}
