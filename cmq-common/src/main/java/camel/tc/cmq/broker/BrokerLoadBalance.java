package camel.tc.cmq.broker;

// done
public interface BrokerLoadBalance {

    BrokerGroupInfo loadBalance(BrokerClusterInfo cluster, BrokerGroupInfo lastGroup);
}
