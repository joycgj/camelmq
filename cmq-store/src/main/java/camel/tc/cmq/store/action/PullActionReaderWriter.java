package camel.tc.cmq.store.action;

import camel.tc.cmq.store.Action;
import camel.tc.cmq.store.ActionReaderWriter;
import camel.tc.cmq.utils.PayloadHolderUtils;

import java.nio.ByteBuffer;
import java.util.Objects;

// done
public class PullActionReaderWriter implements ActionReaderWriter {

    private static final byte TRUE_BYTE = (byte) 1;
    private static final byte FALSE_BYTE = (byte) 0;

    @Override
    public int write(final ByteBuffer to, final Action action) {
        final int startIndex = to.position();

        final PullAction pull = (PullAction) action;
        PayloadHolderUtils.writeString(pull.subject(), to);
        PayloadHolderUtils.writeString(pull.group(), to);
        PayloadHolderUtils.writeString(pull.consumerId(), to);

        to.putLong(action.timestamp());
        to.put(toByte(pull.isBroadcast()));

        to.putLong(pull.getFirstSequence());
        to.putLong(pull.getLastSequence());

        to.putLong(pull.getFirstMessageSequence());
        to.putLong(pull.getLastMessageSequence());

        return to.position() - startIndex;
    }

    @Override
    public PullAction read(final ByteBuffer from) {
        final String subject = PayloadHolderUtils.readString(from);
        final String group = PayloadHolderUtils.readString(from);
        final String consumerId = PayloadHolderUtils.readString(from);

        final long timestamp = from.getLong();
        final boolean broadcast = fromByte(from.get());

        final long firstSequence = from.getLong();
        final long lastSequence = from.getLong();

        final long firstMessageSequence = from.getLong();
        final long lastMessageSequence = from.getLong();

        return new PullAction(subject, group, consumerId, timestamp, broadcast, firstSequence, lastSequence, firstMessageSequence, lastMessageSequence);
    }

    private byte toByte(final boolean bool) {
        return bool ? TRUE_BYTE : FALSE_BYTE;
    }

    private boolean fromByte(final byte b) {
        return Objects.equals(b, TRUE_BYTE);
    }
}
