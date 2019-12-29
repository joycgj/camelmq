package camel.tc.cmq.service.exceptions;

// done
public class DuplicateMessageException extends MessageException {

    private static final long serialVersionUID = 8267606930373695631L;

    public DuplicateMessageException(String messageId) {
        super(messageId, "Duplicated message");
    }
}
