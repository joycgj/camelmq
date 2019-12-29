package camel.tc.cmq.store;

// done
public enum AppendMessageStatus {

    SUCCESS,
    END_OF_FILE,
    DATA_OVERFLOW,
    APPEND_FAILED,
    MESSAGE_SIZE_EXCEEDED,
    UNKNOWN_ERROR
}
