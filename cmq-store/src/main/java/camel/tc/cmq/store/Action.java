package camel.tc.cmq.store;

// done
public interface Action {

    ActionType type();

    String subject();

    String group();

    String consumerId();

    long timestamp();
}
