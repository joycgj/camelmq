package camel.tc.cmq.base;

// done
public class QueryConsumerLagRequest {

    private String subject;
    private String group;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
