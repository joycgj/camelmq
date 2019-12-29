package camel.tc.cmq.netty.exception;

import camel.tc.cmq.service.exceptions.MessageException;

// done
public class BrokerRejectException extends MessageException {

    public BrokerRejectException(String messageId) {
        super(messageId, "reject");
    }
}
