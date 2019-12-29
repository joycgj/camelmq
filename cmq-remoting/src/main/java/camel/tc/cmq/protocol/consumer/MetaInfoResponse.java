package camel.tc.cmq.protocol.consumer;

import camel.tc.cmq.meta.BrokerCluster;
import camel.tc.cmq.base.OnOfflineState;

// done
public class MetaInfoResponse {

    private long timestamp;
    private String subject;
    private String consumerGroup;
    private OnOfflineState onOfflineState;
    private int clientTypeCode;
    private BrokerCluster brokerCluster;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public OnOfflineState getOnOfflineState() {
        return onOfflineState;
    }

    public void setOnOfflineState(OnOfflineState onOfflineState) {
        this.onOfflineState = onOfflineState;
    }

    public int getClientTypeCode() {
        return clientTypeCode;
    }

    public void setClientTypeCode(int clientTypeCode) {
        this.clientTypeCode = clientTypeCode;
    }

    public BrokerCluster getBrokerCluster() {
        return brokerCluster;
    }

    public void setBrokerCluster(BrokerCluster brokerCluster) {
        this.brokerCluster = brokerCluster;
    }

    @Override
    public String toString() {
        return "MetaInfoResponse{" +
                "timestamp=" + timestamp +
                ", subject='" + subject + '\'' +
                ", consumerGroup='" + consumerGroup + '\'' +
                ", onOfflineState=" + onOfflineState +
                ", clientTypeCode=" + clientTypeCode +
                ", brokerCluster=" + brokerCluster +
                '}';
    }
}
