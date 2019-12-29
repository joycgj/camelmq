package camel.tc.cmq.netty.exception;

import camel.tc.cmq.service.exceptions.MessageException;

// done
public class SubjectNotAssignedException extends MessageException {

    public SubjectNotAssignedException(String messageId) {
        super(messageId, "subject not assigned");
    }

    @Override
    public boolean isSubjectNotAssigned() {
        return true;
    }
}
