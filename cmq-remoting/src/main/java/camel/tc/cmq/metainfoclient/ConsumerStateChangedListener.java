package camel.tc.cmq.metainfoclient;

// done
public interface ConsumerStateChangedListener {

    void online(String subject, String consumerGroup);

    void offline(String subject, String consumerGroup);
}
