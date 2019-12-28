package camel.tc.cmq.broker.impl;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import camel.tc.cmq.broker.BrokerClusterInfo;
import camel.tc.cmq.broker.BrokerGroupInfo;
import camel.tc.cmq.broker.BrokerLoadBalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

// done
public class PollBrokerLoadBalance implements BrokerLoadBalance {

    private static final Supplier<BrokerLoadBalance> SUPPLIER = Suppliers.memoize(new Supplier<BrokerLoadBalance>() {
        @Override
        public BrokerLoadBalance get() {
            return new PollBrokerLoadBalance();
        }
    });

    public static BrokerLoadBalance getInstance() {
        return SUPPLIER.get();
    }

    private PollBrokerLoadBalance() {
    }

    @Override
    public BrokerGroupInfo loadBalance(BrokerClusterInfo cluster, BrokerGroupInfo lastGroup) {
        List<BrokerGroupInfo> groups = cluster.getGroups();
        if (lastGroup == null || lastGroup.getGroupIndex() < 0 || lastGroup.getGroupIndex() >= groups.size()) {
            BrokerGroupInfo group;
            for (int i = 0; i < groups.size(); i++) {
                if ((group = selectRandom(groups)).isAvailable()) {
                    return group;
                }
            }
            for (BrokerGroupInfo groupInfo : groups) {
                if (groupInfo.isAvailable()) {
                    return groupInfo;
                }
            }
        } else {
            int index = lastGroup.getGroupIndex();
            for (int count = groups.size(); count > 0; count--) {
                index = (index + 1) % groups.size();
                BrokerGroupInfo nextGroup = groups.get(index);
                if (nextGroup.isAvailable()) {
                    return nextGroup;
                }
            }
        }
        return lastGroup;
    }

    private BrokerGroupInfo selectRandom(List<BrokerGroupInfo> groups) {
        int random = ThreadLocalRandom.current().nextInt(groups.size());
        return groups.get(random);
    }
}
