package camel.tc.cmq.protocol.consumer;

import com.google.common.base.Strings;
import camel.tc.cmq.base.ClientRequestType;
import camel.tc.cmq.common.ClientType;

import java.util.HashMap;
import java.util.Map;

// done
public class MetaInfoRequest {

    private static final String SUBJECT = "subject";
    private static final String CLIENT_TYPE_CODE = "clientTypeCode";
    private static final String APP_CODE = "appCode";
    private static final String CLIENT_ID = "clientId";
    private static final String CONSUMER_GROUP = "consumerGroup";
    private static final String REQUEST_TYPE = "requestType";

    private final Map<String, String> attrs;

    public MetaInfoRequest() {
        this.attrs = new HashMap<>();
    }

    public MetaInfoRequest(Map<String, String> attrs) {
        this.attrs = new HashMap<>(attrs);
    }

    Map<String, String> getAttrs() {
        return attrs;
    }

    public String getSubject() {
        return Strings.nullToEmpty(attrs.get(SUBJECT));
    }

    public void setSubject(String subject) {
        setStringValue(SUBJECT, subject);
    }

    public int getClientTypeCode() {
        return getIntValue(CLIENT_TYPE_CODE, ClientType.OTHER.getCode());
    }

    public void setClientType(ClientType clientType) {
        setIntValue(CLIENT_TYPE_CODE, clientType.getCode());
    }

    public String getAppCode() {
        return getStringValue(APP_CODE);
    }

    public void setAppCode(String appCode) {
        setStringValue(APP_CODE, appCode);
    }

    public String getClientId() {
        return Strings.nullToEmpty(attrs.get(CLIENT_ID));
    }

    public void setClientId(String clientId) {
        setStringValue(CLIENT_ID, clientId);
    }

    public String getConsumerGroup() {
        return Strings.nullToEmpty(attrs.get(CONSUMER_GROUP));
    }

    public void setConsumerGroup(String consumerGroup) {
        setStringValue(CONSUMER_GROUP, consumerGroup);
    }

    public int getRequestType() {
        return getIntValue(REQUEST_TYPE, ClientRequestType.ONLINE.getCode());
    }

    public void setRequestType(ClientRequestType requestType) {
        setIntValue(REQUEST_TYPE, requestType.getCode());
    }

    private void setIntValue(String attrName, int value) {
        attrs.put(attrName, Integer.toString(value));
    }

    private int getIntValue(String attrName, int defaultValue) {
        try {
            return Integer.parseInt(attrs.get(attrName));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private void setStringValue(String attrName, String value) {
        attrs.put(attrName, Strings.nullToEmpty(value));
    }

    private String getStringValue(String attrName) {
        return Strings.nullToEmpty(attrs.get(attrName));
    }

    @Override
    public String toString() {
        return "MetaInfoRequest{" + "attrs='" + attrs + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetaInfoRequest)) return false;

        MetaInfoRequest that = (MetaInfoRequest) o;

        return attrs != null ? attrs.equals(that.attrs) : that.attrs == null;
    }

    @Override
    public int hashCode() {
        return attrs != null ? attrs.hashCode() : 0;
    }
}
