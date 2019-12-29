package camel.tc.cmq.meta;

import java.util.List;

// done
public class BrokerRegisterResponse {

    private List<String> subjects;

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }
}
