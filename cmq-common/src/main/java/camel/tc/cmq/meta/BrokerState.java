package camel.tc.cmq.meta;

// done
public enum BrokerState {

    RW(1), R(2), W(3), NRW(4);

    private final int code;

    BrokerState(int code) {
        this.code = code;
    }

    public static BrokerState codeOf(int brokerState) {
        for (BrokerState value : BrokerState.values()) {
            if (value.getCode() == brokerState) {
                return value;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public boolean canRead() {
        return code == RW.code || code == R.code;
    }

    public boolean canWrite() {
        return code == RW.code || code == W.code;
    }
}
