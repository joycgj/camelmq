package camel.tc.cmq.store;

// done
public enum GetMessageStatus {

    SUCCESS,
    OFFSET_OVERFLOW,
    NO_MESSAGE,
    SUBJECT_NOT_FOUND,
    EMPTY_CONSUMER_LOG,


    SEQUENCE_TOO_SMALL,
    SEQUENCE_TOO_LARGE,
    TABLE_ALREADY_EVICTED,
}
