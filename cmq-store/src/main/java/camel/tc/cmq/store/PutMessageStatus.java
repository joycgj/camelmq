package camel.tc.cmq.store;

// done
public enum PutMessageStatus {

    SUCCESS(0),
    CREATE_MAPPED_FILE_FAILED(1),
    MESSAGE_ILLEGAL(2),
    ALREADY_WRITTEN(3),
    UNKNOWN_ERROR(-1);

    private int code;

    PutMessageStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}