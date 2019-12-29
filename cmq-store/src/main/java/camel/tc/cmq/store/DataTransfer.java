package camel.tc.cmq.store;

import io.netty.buffer.ByteBuf;
import io.netty.channel.FileRegion;
import io.netty.util.ReferenceCounted;
import camel.tc.cmq.store.buffer.SegmentBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.WritableByteChannel;

// done
public class DataTransfer implements FileRegion {

    private final ByteBuf headerBuffer;
    private final SegmentBuffer segmentBuffer;
    private final int bufferTotalSize;

    private final ByteBuffer[] buffers;

    private long transferred;

    public DataTransfer(ByteBuf headerBuffer, SegmentBuffer segmentBuffer, int bufferTotalSize) {
        this.headerBuffer = headerBuffer;
        this.segmentBuffer = segmentBuffer;
        this.bufferTotalSize = bufferTotalSize;

        this.buffers = new ByteBuffer[2];
        this.buffers[0] = headerBuffer.nioBuffer();
        this.buffers[1] = segmentBuffer.getBuffer();
    }

    @Override
    public long position() {
        long pos = 0;
        for (ByteBuffer buffer : this.buffers) {
            pos += buffer.position();
        }
        return pos;
    }

    @Override
    public long transfered() {
        return transferred;
    }

    @Override
    public long count() {
        return headerBuffer.readableBytes() + bufferTotalSize;
    }

    @Override
    public long transferTo(WritableByteChannel target, long position) throws IOException {
        GatheringByteChannel channel = (GatheringByteChannel) target;
        long write = channel.write(this.buffers);
        transferred += write;
        return write;
    }


    @Override
    public int refCnt() {
        return 0;
    }

    @Override
    public ReferenceCounted retain() {
        return null;
    }

    @Override
    public ReferenceCounted retain(int increment) {
        return null;
    }

    @Override
    public boolean release() {
        headerBuffer.release();
        segmentBuffer.release();
        return true;
    }

    @Override
    public boolean release(int decrement) {
        return release();
    }
}
