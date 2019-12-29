package camel.tc.cmq.store;

// done
public enum ConsumeFromWhere {

    UNKNOWN(0), LATEST(1), EARLIEST(2);

    private int code;

    ConsumeFromWhere(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ConsumeFromWhere codeOf(int code) {
        for (ConsumeFromWhere consumeFromWhere : ConsumeFromWhere.values()) {
            if (consumeFromWhere.code == code) {
                return consumeFromWhere;
            }
        }
        return ConsumeFromWhere.UNKNOWN;
    }
}
