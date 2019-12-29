package camel.tc.cmq.store;

// done
public class GroupAndSubject {

    private static final String GROUP_INDEX_DELIM = "@";

    private final String subject;

    private final String group;

    public GroupAndSubject(String subject, String group) {
        this.subject = subject;
        this.group = group;
    }

    public static String groupAndSubject(String subject, String group) {
        return group + GROUP_INDEX_DELIM + subject;
    }

    public static GroupAndSubject parse(String groupAndSubject) {
        String[] arr = groupAndSubject.split(GROUP_INDEX_DELIM);
        return new GroupAndSubject(arr[1], arr[0]);
    }

    public String getSubject() {
        return subject;
    }

    public String getGroup() {
        return group;
    }
}
