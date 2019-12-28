package camel.tc.cmq;

// done
public interface ProduceMessage {

    String getMessageId();

    String getSubject();

    void send();

    void error(Exception e);

    void failed();

    void block();

    void finish();

    Message getBase();

    void startSendTrace();

    void setStore(MessageStore messageStore);

    void save();

    long getSequence();

    void setRouteKey(Object routeKey);

    Object getRouteKey();
}
