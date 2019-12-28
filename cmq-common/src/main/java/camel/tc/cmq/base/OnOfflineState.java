package camel.tc.cmq.base;

// done
public enum OnOfflineState {

    ONLINE(0), OFFLINE(1);

    private final int code;

    OnOfflineState(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

    @Override
    public String toString() {
        return code == 0 ? "ON" : "OFF";
    }

    public static OnOfflineState fromCode(int code) {
        return code == 0 ? ONLINE : OFFLINE;
    }
}
