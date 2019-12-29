package camel.tc.cmq.store;

import java.nio.ByteBuffer;

// done
public interface MessageAppender<T, R> {

    AppendMessageResult<R> doAppend(final long baseOffset, final ByteBuffer targetBuffer, final int freeSpace, final T message);
}
