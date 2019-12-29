package camel.tc.cmq.utils;

import java.nio.ByteBuffer;
import java.util.zip.Checksum;

// done
public final class Checksums {

    private Checksums() {
    }

    public static void update(Checksum checksum, ByteBuffer buffer, int length) {
        update(checksum, buffer, 0, length);
    }

    public static void update(Checksum checksum, ByteBuffer buffer, int offset, int length) {
        if (buffer.hasArray()) {
            checksum.update(buffer.array(), buffer.position() + buffer.arrayOffset() + offset, length);
        } else {
            int start = buffer.position() + offset;
            for (int i = start; i < start + length; i++)
                checksum.update(buffer.get(i));
        }
    }
}
