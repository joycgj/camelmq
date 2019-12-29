package camel.tc.cmq.store.action;

import camel.tc.cmq.store.Action;
import camel.tc.cmq.store.ActionReaderWriter;
import camel.tc.cmq.utils.PayloadHolderUtils;

import java.nio.ByteBuffer;

// done
public class ForeverOfflineActionReaderWriter implements ActionReaderWriter {

    @Override
    public int write(ByteBuffer to, Action action) {
        final int startIndex = to.position();

        PayloadHolderUtils.writeString(action.subject(), to);
        PayloadHolderUtils.writeString(action.group(), to);
        PayloadHolderUtils.writeString(action.consumerId(), to);
        to.putLong(action.timestamp());

        return to.position() - startIndex;
    }

    @Override
    public Action read(ByteBuffer from) {
        final String subject = PayloadHolderUtils.readString(from);
        final String group = PayloadHolderUtils.readString(from);
        final String consumerId = PayloadHolderUtils.readString(from);

        final long timestamp = from.getLong();
        return new ForeverOfflineAction(subject, group, consumerId, timestamp);
    }
}
