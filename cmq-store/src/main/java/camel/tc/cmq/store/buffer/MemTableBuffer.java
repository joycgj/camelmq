package camel.tc.cmq.store.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.ReferenceCountUtil;

import java.nio.ByteBuffer;

// done
public class MemTableBuffer implements Buffer {

    private final ByteBuf buf;
    private final int size;

    public MemTableBuffer(final ByteBuf buf, final int size) {
        this.buf = buf;
        this.size = size;
    }

    @Override
    public ByteBuffer getBuffer() {
        return buf.nioBuffer();
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean retain() {
        try {
            buf.retain();
            return true;
        } catch (IllegalReferenceCountException ignore) {
            return false;
        }
    }

    @Override
    public boolean release() {
        ReferenceCountUtil.safeRelease(buf);
        return true;
    }
}
