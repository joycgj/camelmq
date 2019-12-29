package camel.tc.cmq.meta;

// done
public enum BrokerGroupKind {

    NORMAL(1),
    DELAY(2);

    private final int code;

    BrokerGroupKind(final int code) {
        this.code = code;
    }

    public static BrokerGroupKind fromCode(final int code) {
        for (final BrokerGroupKind kind : values()) {
            if (kind.getCode() == code) {
                return kind;
            }
        }

        throw new RuntimeException("unknown broker group kind code " + code);
    }

    public int getCode() {
        return code;
    }
}
