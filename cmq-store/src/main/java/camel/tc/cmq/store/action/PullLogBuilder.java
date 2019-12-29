package camel.tc.cmq.store.action;

import camel.tc.cmq.store.Storage;
import camel.tc.cmq.store.PullLogMessage;
import camel.tc.cmq.store.event.FixedExecOrderEventBus;

import java.util.ArrayList;
import java.util.List;

// done
public class PullLogBuilder implements FixedExecOrderEventBus.Listener<ActionEvent> {
    
    private final Storage storage;

    public PullLogBuilder(final Storage storage) {
        this.storage = storage;
    }

    @Override
    public void onEvent(final ActionEvent event) {
        switch (event.getAction().type()) {
            case PULL:
                buildPullLog(event);
                break;
        }
    }

    private void buildPullLog(ActionEvent event) {
        final PullAction action = (PullAction) event.getAction();
        if (action.isBroadcast()) return;

        if (action.getFirstSequence() - action.getLastSequence() > 0) return;
        storage.putPullLogs(action.subject(), action.group(), action.consumerId(), createMessages(action));
    }

    private List<PullLogMessage> createMessages(final PullAction action) {
        final int count = (int) (action.getLastSequence() - action.getFirstSequence() + 1);
        final List<PullLogMessage> messages = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            messages.add(new PullLogMessage(action.getFirstSequence() + i, action.getFirstMessageSequence() + i));
        }

        return messages;
    }
}
