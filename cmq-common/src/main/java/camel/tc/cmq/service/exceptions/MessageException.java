package camel.tc.cmq.service.exceptions;

// done
public class MessageException extends Exception {

    public static final String BROKER_BUSY = "broker busy";
    public static final String REJECT_MESSAGE = "message rejected";
    public static final String UNKNOWN_MESSAGE = "unknown exception";

    private static final long serialVersionUID = -8385014158365588186L;

    private final String messageId;

    public MessageException(String messageId, String msg, Throwable t) {
        super(msg, t);
        this.messageId = messageId;
    }

    public MessageException(String messageId, String msg) {
        this(messageId, msg, null);
    }

    public String getMessageId() {
        return messageId;
    }

    @Override
    public Throwable initCause(Throwable cause) {
        return this;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    public boolean isBrokerBusy() {
        return BROKER_BUSY.equals(getMessage());
    }

    public boolean isSubjectNotAssigned() {
        return false;
    }

    public boolean isRejected() {
        return REJECT_MESSAGE.equals(getMessage());
    }
}
