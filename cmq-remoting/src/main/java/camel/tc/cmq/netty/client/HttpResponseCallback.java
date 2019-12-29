package camel.tc.cmq.netty.client;

// done
public interface HttpResponseCallback<V> {

    V onCompleted(Response response) throws Exception;

    void onThrowable(Throwable t);
}
