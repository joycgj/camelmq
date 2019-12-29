package camel.tc.cmq.protocol.consumer;

// done
public enum PullFilterType {

    TAG((short) 1),
    SUB_ENV_ISOLATION((short) 2);

    private final short code;

    PullFilterType(final short code) {
        this.code = code;
    }

    public static PullFilterType fromCode(final short code) {
        for (final PullFilterType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }

        throw new RuntimeException("unknown pull filter type code " + code);
    }

    public short getCode() {
        return code;
    }
}
