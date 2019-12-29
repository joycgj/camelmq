package camel.tc.cmq.store;

import camel.tc.cmq.store.buffer.SegmentBuffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static camel.tc.cmq.store.MessageLog.*;

// done
public class MessageLogRecordVisitor extends AbstractLogVisitor<MessageLogRecord> {

    MessageLogRecordVisitor(final LogManager logManager, final long startOffset) {
        super(logManager, startOffset);
    }

    @Override
    protected LogVisitorRecord<MessageLogRecord> readOneRecord(final SegmentBuffer segmentBuffer) {
        ByteBuffer buffer = segmentBuffer.getBuffer();
        if (buffer.remaining() < MIN_RECORD_BYTES) {
            return LogVisitorRecord.noMore();
        }

        final int startPos = buffer.position();
        final long startWrotePos = getStartOffset() + startPos;
        final int magic = buffer.getInt();
        if (!MagicCodeSupport.isValidMessageLogMagicCode(magic)) {
            setVisitedBufferSize(getBufferSize());
            return LogVisitorRecord.noMore();
        }

        final byte attributes = buffer.get();
        buffer.getLong();
        if (attributes == ATTR_BLANK_RECORD) {
            if (buffer.remaining() < Integer.BYTES) {
                return LogVisitorRecord.noMore();
            }
            final int blankSize = buffer.getInt();
            incrVisitedBufferSize(blankSize + (buffer.position() - startPos));
            return LogVisitorRecord.empty();
        } else if (attributes == ATTR_EMPTY_RECORD) {
            setVisitedBufferSize(getBufferSize());
            return LogVisitorRecord.noMore();
        } else {
            if (buffer.remaining() < (Long.BYTES + Integer.BYTES)) {
                return LogVisitorRecord.noMore();
            }
            final long sequence = buffer.getLong();
            final int subjectSize = buffer.getShort();
            if (buffer.remaining() < subjectSize) {
                return LogVisitorRecord.noMore();
            }
            final byte[] subjectBytes = new byte[subjectSize];
            buffer.get(subjectBytes);

            if (magic >= MagicCode.MESSAGE_LOG_MAGIC_V2) {
                if (buffer.remaining() < Long.BYTES) {
                    return LogVisitorRecord.noMore();
                }
                //skip crc
                buffer.getLong();
            }

            if (buffer.remaining() < Integer.BYTES) {
                return LogVisitorRecord.noMore();
            }
            final int payloadSize = buffer.getInt();
            if (buffer.remaining() < payloadSize) {
                return LogVisitorRecord.noMore();
            }
            final short headerSize = (short) (buffer.position() - startPos);
            final ByteBuffer payload = buffer.slice();
            payload.limit(payloadSize);

            buffer.position(buffer.position() + payloadSize);

            final int recordBytes = buffer.position() - startPos;
            incrVisitedBufferSize(recordBytes);
            final String subject = new String(subjectBytes, StandardCharsets.UTF_8);
            return LogVisitorRecord.data(new MessageLogRecord(subject, sequence, startWrotePos, recordBytes, headerSize, getBaseOffset(), payload, segmentBuffer.getLogSegment()));
        }
    }
}
