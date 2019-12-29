package camel.tc.cmq.meta;

// done
public enum BrokerRole {

    MASTER(BrokerGroupKind.NORMAL, 0),
    SLAVE(BrokerGroupKind.NORMAL, 1),
    STANDBY(BrokerGroupKind.NORMAL, 2),
    DELAY(BrokerGroupKind.DELAY, 3),
    BACKUP(BrokerGroupKind.NORMAL, 4),
    DELAY_MASTER(BrokerGroupKind.DELAY, 5),
    DELAY_SLAVE(BrokerGroupKind.DELAY, 6),
    DELAY_BACKUP(BrokerGroupKind.DELAY, 7);

    private final BrokerGroupKind kind;
    private final int code;

    BrokerRole(final BrokerGroupKind kind, final int code) {
        this.kind = kind;
        this.code = code;
    }

    public static BrokerRole fromCode(int role) {
        for (BrokerRole value : BrokerRole.values()) {
            if (value.getCode() == role) {
                return value;
            }
        }

        throw new RuntimeException("Unknown broker role code " + role);
    }

    public BrokerGroupKind getKind() {
        return kind;
    }

    public int getCode() {
        return code;
    }
}
