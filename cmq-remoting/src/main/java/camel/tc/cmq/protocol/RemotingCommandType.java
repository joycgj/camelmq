package camel.tc.cmq.protocol;

// done
public enum RemotingCommandType {

    REQUEST_COMMAND(0),
    RESPONSE_COMMAND(1);

    private int code;

    RemotingCommandType(int flag) {
        this.code = flag;
    }

    public static RemotingCommandType codeOf(int code) {
        for(RemotingCommandType domainType : RemotingCommandType.values()) {
            if(domainType.code == code) {
                return domainType;
            }
        }
        throw new RuntimeException("Unsupported Command code");
    }

    public int getCode() {
        return code;
    }
}
