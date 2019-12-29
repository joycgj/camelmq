package camel.tc.cmq.service.exceptions;

// done
public class BlockMessageException extends MessageException {

    private static final long serialVersionUID = 1068741830127606624L;

    public BlockMessageException(String messageId) {
        super(messageId, "block message");
    }
}
