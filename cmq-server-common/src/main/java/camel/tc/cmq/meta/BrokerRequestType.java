package camel.tc.cmq.meta;

// done
public enum BrokerRequestType {

    ONLINE(0), HEARTBEAT(1), OFFLINE(2);

    private int code;

    BrokerRequestType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
