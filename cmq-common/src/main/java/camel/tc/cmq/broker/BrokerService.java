package camel.tc.cmq.broker;

import camel.tc.cmq.common.ClientType;

// done
public interface BrokerService {

    BrokerClusterInfo getClusterBySubject(ClientType clientType, String subject);

    BrokerClusterInfo getClusterBySubject(ClientType clientType, String subject, String group);

    void refresh(ClientType clientType, String subject);

    void refresh(ClientType clientType, String subject, String group);

    void setAppCode(String appCode);
}
