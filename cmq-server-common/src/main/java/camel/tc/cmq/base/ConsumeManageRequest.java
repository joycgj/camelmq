package camel.tc.cmq.base;

// done
public class ConsumeManageRequest {

    private int consumerFromWhere;
    private String subject;
    private String group;

    public int getConsumerFromWhere() {
        return consumerFromWhere;
    }

    public void setConsumerFromWhere(int consumerFromWhere) {
        this.consumerFromWhere = consumerFromWhere;
    }

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

    @Override
    public String toString() {
        return "ConsumeManageRequest{" +
                "consumerFromWhere=" + consumerFromWhere +
                ", subject='" + subject + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
