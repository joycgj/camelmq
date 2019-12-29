package camel.tc.cmq.meta;

import java.util.List;

// done
public class BrokerCluster {

    private List<BrokerGroup> brokerGroups;

    public BrokerCluster(List<BrokerGroup> brokerGroups) {
        this.brokerGroups = brokerGroups;
    }

    public List<BrokerGroup> getBrokerGroups() {
        return brokerGroups;
    }

    public void setBrokerGroups(List<BrokerGroup> brokerGroups) {
        this.brokerGroups = brokerGroups;
    }
}
