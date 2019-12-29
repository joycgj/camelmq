package camel.tc.cmq.store.action;

import camel.tc.cmq.store.Action;
import camel.tc.cmq.store.ActionType;

// done
public class ForeverOfflineAction implements Action {

    private final String subject;
    private final String group;
    private final String consumerId;
    private final long timestamp;

    public ForeverOfflineAction(String subject, String group, String consumerId, long timestamp) {
        this.subject = subject;
        this.group = group;
        this.consumerId = consumerId;
        this.timestamp = timestamp;
    }


    @Override
    public ActionType type() {
        return ActionType.FOREVER_OFFLINE;
    }

    @Override
    public String subject() {
        return subject;
    }

    @Override
    public String group() {
        return group;
    }

    @Override
    public String consumerId() {
        return consumerId;
    }

    @Override
    public long timestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "ForeverOfflineAction{" +
                "subject='" + subject + '\'' +
                ", group='" + group + '\'' +
                ", consumerId='" + consumerId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
