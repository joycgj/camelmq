package camel.tc.cmq.store;

// done
public class ConsumerLogWroteEvent {

    private final String subject;
    private final boolean success;

    public ConsumerLogWroteEvent(String subject, boolean success) {
        this.subject = subject;
        this.success = success;
    }

    public String getSubject() {
        return subject;
    }

    public boolean isSuccess() {
        return success;
    }
}
