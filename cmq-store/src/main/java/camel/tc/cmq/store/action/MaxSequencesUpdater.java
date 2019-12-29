package camel.tc.cmq.store.action;

import camel.tc.cmq.store.CheckpointManager;
import camel.tc.cmq.store.event.FixedExecOrderEventBus;

// done
public class MaxSequencesUpdater implements FixedExecOrderEventBus.Listener<ActionEvent> {

    private final CheckpointManager manager;

    public MaxSequencesUpdater(final CheckpointManager manager) {
        this.manager = manager;
    }

    @Override
    public void onEvent(final ActionEvent event) {
        final long offset = event.getOffset();

        switch (event.getAction().type()) {
            case PULL:
                manager.updateActionReplayState(offset, (PullAction) event.getAction());
                break;
            case RANGE_ACK:
                manager.updateActionReplayState(offset, (RangeAckAction) event.getAction());
                break;
        }
    }
}
