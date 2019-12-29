package camel.tc.cmq.store.action;

import camel.tc.cmq.store.Action;
import camel.tc.cmq.store.ActionReaderWriter;
import camel.tc.cmq.utils.PayloadHolderUtils;

import java.nio.ByteBuffer;

// done
public class RangeAckActionReaderWriter implements ActionReaderWriter {

    @Override
    public int write(ByteBuffer to, Action action) {
        final int startIndex = to.position();

        final RangeAckAction rangeAck = (RangeAckAction) action;
        PayloadHolderUtils.writeString(rangeAck.subject(), to);
        PayloadHolderUtils.writeString(rangeAck.group(), to);
        PayloadHolderUtils.writeString(rangeAck.consumerId(), to);
        to.putLong(action.timestamp());
        to.putLong(rangeAck.getFirstSequence());
        to.putLong(rangeAck.getLastSequence());
        return to.position() - startIndex;
    }

    @Override
    public RangeAckAction read(final ByteBuffer from) {
        final String subject = PayloadHolderUtils.readString(from);
        final String group = PayloadHolderUtils.readString(from);
        final String consumerId = PayloadHolderUtils.readString(from);
        final long timestamp = from.getLong();
        final long firstSequence = from.getLong();
        final long lastSequence = from.getLong();

        return new RangeAckAction(subject, group, consumerId, timestamp, firstSequence, lastSequence);
    }
}
