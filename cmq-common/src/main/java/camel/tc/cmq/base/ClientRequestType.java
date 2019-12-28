package camel.tc.cmq.base;

// done
public enum ClientRequestType {

    ONLINE(1), HEARTBEAT(2);

    private int code;

    ClientRequestType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

