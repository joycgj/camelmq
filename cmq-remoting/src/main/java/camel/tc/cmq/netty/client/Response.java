package camel.tc.cmq.netty.client;

// done
public interface Response {

    int getStatusCode();

    String getHeader(String name);

    String getBody();
}
