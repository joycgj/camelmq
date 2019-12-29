package camel.tc.cmq.store;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import camel.tc.cmq.monitor.QMon;
import camel.tc.cmq.store.action.ActionEvent;
import camel.tc.cmq.store.buffer.SegmentBuffer;

import java.io.File;
import java.nio.ByteBuffer;

// done
public class ActionLog implements Visitable<ActionEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ActionLog.class);

    public static final int PER_SEGMENT_FILE_SIZE = 100 * 1024 * 1024;
    public static final int MIN_RECORD_BYTES = 5; // 4 bytes magic + 1 byte record type

    public static final byte ATTR_BLANK_RECORD = 2;
    public static final byte ATTR_EMPTY_RECORD = 1;
    public static final byte ATTR_ACTION_RECORD = 0;

    private final StorageConfig config;
    private final LogManager logManager;
    private final MessageAppender<Action, MessageSequence> actionAppender = new ActionAppender();

    public ActionLog(final StorageConfig config) {
        this.config = config;
        this.logManager = new LogManager(new File(config.getActionLogStorePath()), PER_SEGMENT_FILE_SIZE, new ActionLogSegmentValidator());
    }

    public synchronized PutMessageResult addAction(final Action action) {
        final AppendMessageResult<MessageSequence> result;
        LogSegment segment = logManager.latestSegment();
        if (segment == null) {
            segment = logManager.allocNextSegment();
        }

        if (segment == null) {
            return new PutMessageResult(PutMessageStatus.CREATE_MAPPED_FILE_FAILED, null);
        }

        result = segment.append(action, actionAppender);
        switch (result.getStatus()) {
            case SUCCESS:
                break;
            case END_OF_FILE:
                if (logManager.allocNextSegment() == null) {
                    return new PutMessageResult(PutMessageStatus.CREATE_MAPPED_FILE_FAILED, null);
                }
                return addAction(action);
            case MESSAGE_SIZE_EXCEEDED:
                return new PutMessageResult(PutMessageStatus.MESSAGE_ILLEGAL, result);
            default:
                return new PutMessageResult(PutMessageStatus.UNKNOWN_ERROR, result);
        }

        return new PutMessageResult(PutMessageStatus.SUCCESS, result);
    }

    public boolean appendData(final long startOffset, final ByteBuffer data) {
        LogSegment segment = logManager.locateSegment(startOffset);
        if (segment == null) {
            segment = logManager.allocOrResetSegments(startOffset);
            fillPreBlank(segment, startOffset);
        }

        return segment.appendData(data);
    }

    private void fillPreBlank(LogSegment segment, long untilWhere) {
        final ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.putInt(MagicCode.ACTION_LOG_MAGIC_V1);
        buffer.put((byte) 2);
        buffer.putInt((int) (untilWhere % PER_SEGMENT_FILE_SIZE));
        segment.fillPreBlank(buffer, untilWhere);
    }

    public SegmentBuffer getMessageData(final long offset) {
        final LogSegment segment = logManager.locateSegment(offset);
        if (segment == null) {
            return null;
        }

        final int pos = (int) (offset % PER_SEGMENT_FILE_SIZE);
        return segment.selectSegmentBuffer(pos);
    }

    @Override
    public ActionLogVisitor newVisitor(final long start) {
        return new ActionLogVisitor(logManager, start);
    }

    @Override
    public long getMaxOffset() {
        return logManager.getMaxOffset();
    }

    @Override
    public long getMinOffset() {
        return logManager.getMinOffset();
    }

    public void flush() {
        final long start = System.currentTimeMillis();
        try {
            logManager.flush();
        } finally {
            QMon.flushActionLogTimer(System.currentTimeMillis() - start);
        }
    }

    public void close() {
        logManager.close();
    }

    public void clean() {
        logManager.deleteExpiredSegments(config.getLogRetentionMs());
    }

    private class ActionAppender implements MessageAppender<Action, MessageSequence> {
        private static final int MIN_RECORD_BYTES = 5; // 4 bytes magic + 1 byte record type
        private static final int MAX_BYTES = 1024 * 1024 * 10; // 10M

        private final ByteBuffer workingBuffer = ByteBuffer.allocate(MAX_BYTES);

        @Override
        public AppendMessageResult<MessageSequence> doAppend(long baseOffset, ByteBuffer targetBuffer, int freeSpace, Action action) {
            workingBuffer.clear();
            final int size = fillBuffer(workingBuffer, action);
            final long wroteOffset = baseOffset + targetBuffer.position();

            if (size != freeSpace && size + MIN_RECORD_BYTES > freeSpace) {
                workingBuffer.clear();
                workingBuffer.limit(freeSpace);
                workingBuffer.putInt(MagicCode.ACTION_LOG_MAGIC_V1);
                workingBuffer.put((byte) 1);
                targetBuffer.put(workingBuffer.array(), 0, freeSpace);
                return new AppendMessageResult<>(AppendMessageStatus.END_OF_FILE, wroteOffset, freeSpace, null);
            } else {
                workingBuffer.limit(size);
                targetBuffer.put(workingBuffer.array(), 0, size);

                return new AppendMessageResult<>(AppendMessageStatus.SUCCESS, wroteOffset, size, new MessageSequence(wroteOffset, wroteOffset));
            }
        }

        private int fillBuffer(final ByteBuffer buffer, final Action action) {
            final int startIndex = buffer.position();
            buffer.putInt(MagicCode.ACTION_LOG_MAGIC_V1);
            buffer.put((byte) 0);
            buffer.put(action.type().getCode());

            final int payloadSizeIndex = buffer.position();
            buffer.position(buffer.position() + Integer.BYTES);

            // TODO(keli.wang): add monitor here
            final int payloadSize = action.type().getReaderWriter().write(buffer, action);

            buffer.putInt(payloadSizeIndex, payloadSize);

            return buffer.position() - startIndex;
        }
    }

    private class ActionLogSegmentValidator implements LogSegmentValidator {
        @Override
        public ValidateResult validate(LogSegment segment) {
            final int fileSize = segment.getFileSize();
            final ByteBuffer buffer = segment.sliceByteBuffer();

            int position = 0;
            while (true) {
                if (position == fileSize) {
                    return new ValidateResult(ValidateStatus.COMPLETE, fileSize);
                }

                final int result = consumeAndValidateMessage(buffer);
                if (result == -1) {
                    return new ValidateResult(ValidateStatus.PARTIAL, position);
                } else if (result == 0) {
                    return new ValidateResult(ValidateStatus.COMPLETE, fileSize);
                } else {
                    position += result;
                }
            }
        }

        private int consumeAndValidateMessage(final ByteBuffer buffer) {
            final int magic = buffer.getInt();
            if (magic != MagicCode.ACTION_LOG_MAGIC_V1) {
                return -1;
            }

            final byte recordType = buffer.get();
            if (recordType == 2) {
                return buffer.getInt();
            } else if (recordType == 1) {
                return 0;
            } else if (recordType == 0) {
                try {
                    final ActionType payloadType = ActionType.fromCode(buffer.get());
                    final int payloadSize = buffer.getInt();
                    // TODO(keli.wang): 如果我们要校验action是否完全正确，还是得在readerwriter内部进行
                    // 写完action之后得再写一位非0记录，这样才能完全判断记录不是partial的
                    final Action action = payloadType.getReaderWriter().read(buffer);
                    if (Strings.isNullOrEmpty(action.subject())
                            || Strings.isNullOrEmpty(action.group())
                            || Strings.isNullOrEmpty(action.consumerId())
                            || action.timestamp() <= 0) {
                        return -1;
                    }
                    return payloadSize + 10;
                } catch (Exception e) {
                    LOG.error("fail read action log", e);
                    return -1;
                }
            } else {
                return -1;
            }
        }
    }
}
