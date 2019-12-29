package camel.tc.cmq.metainfoclient;

import camel.tc.cmq.meta.MetaServerLocator;
import camel.tc.cmq.protocol.consumer.MetaInfoRequest;
import camel.tc.cmq.protocol.consumer.MetaInfoResponse;

// done
interface MetaInfoClient {
    
    void sendRequest(MetaInfoRequest request);

    void registerResponseSubscriber(ResponseSubscriber receiver);

    void setMetaServerLocator(MetaServerLocator locator);

    interface ResponseSubscriber {
        void onResponse(MetaInfoResponse response);
    }
}
