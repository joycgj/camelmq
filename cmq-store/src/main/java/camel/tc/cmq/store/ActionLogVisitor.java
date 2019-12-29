package camel.tc.cmq.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import camel.tc.cmq.store.action.ActionEvent;
import camel.tc.cmq.store.buffer.SegmentBuffer;

import java.nio.ByteBuffer;

import static camel.tc.cmq.store.ActionLog.*;

// done
public class ActionLogVisitor extends AbstractLogVisitor<ActionEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ActionLogVisitor.class);

    private static final int MIN_RECORD_BYTES = 5; // 4 bytes magic + 1 byte record type

    public ActionLogVisitor(final LogManager logManager, final long startOffset) {
        super(logManager, startOffset);
    }

    @Override
    protected LogVisitorRecord<ActionEvent> readOneRecord(final SegmentBuffer segmentBuffer) {
        ByteBuffer buffer = segmentBuffer.getBuffer();
        if (buffer.remaining() < MIN_RECORD_BYTES) {
            return LogVisitorRecord.noMore();
        }

        final int startPos = buffer.position();
        final int magic = buffer.getInt();
        if (magic != MagicCode.ACTION_LOG_MAGIC_V1) {
            setVisitedBufferSize(getBufferSize());
            return LogVisitorRecord.noMore();
        }

        final byte attributes = buffer.get();
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
        } else if (attributes == ATTR_ACTION_RECORD) {
            try {
                if (buffer.remaining() < Integer.BYTES + Byte.BYTES) {
                    return LogVisitorRecord.noMore();
                }
                final ActionType payloadType = ActionType.fromCode(buffer.get());
                final int payloadSize = buffer.getInt();
                if (buffer.remaining() < payloadSize) {
                    return LogVisitorRecord.noMore();
                }
                final Action action = payloadType.getReaderWriter().read(buffer);
                incrVisitedBufferSize(buffer.position() - startPos);

                return LogVisitorRecord.data(new ActionEvent(getStartOffset() + visitedBufferSize(), action));
            } catch (Exception e) {
                LOG.error("fail read action log", e);
                return LogVisitorRecord.noMore();
            }
        } else {
            throw new RuntimeException("Unknown record type " + attributes);
        }
    }
}
