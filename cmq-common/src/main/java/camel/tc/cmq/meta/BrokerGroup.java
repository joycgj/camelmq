package camel.tc.cmq.meta;

import java.util.List;
import java.util.Objects;

// done
public class BrokerGroup {

    private String groupName;
    private String master;
    private List<String> slaves;
    private long updateTime;
    private BrokerState brokerState;
    private String tag;
    private BrokerGroupKind kind;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public BrokerState getBrokerState() {
        return brokerState;
    }

    public void setBrokerState(BrokerState brokerState) {
        this.brokerState = brokerState;
    }

    public List<String> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<String> slaves) {
        this.slaves = slaves;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public BrokerGroupKind getKind() {
        return kind;
    }

    public void setKind(final BrokerGroupKind kind) {
        this.kind = kind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrokerGroup group = (BrokerGroup) o;
        return Objects.equals(groupName, group.groupName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(groupName);
    }

    @Override
    public String toString() {
        return "BrokerGroup{" +
                "groupName='" + groupName + '\'' +
                ", master='" + master + '\'' +
                ", slaves=" + slaves +
                ", updateTime=" + updateTime +
                ", brokerState=" + brokerState +
                ", tag='" + tag + '\'' +
                '}';
    }
}
