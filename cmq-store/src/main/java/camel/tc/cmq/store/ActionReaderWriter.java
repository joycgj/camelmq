package camel.tc.cmq.store;

import java.nio.ByteBuffer;

// done
public interface ActionReaderWriter {

    int write(final ByteBuffer to, final Action action);

    Action read(final ByteBuffer from);
}
