package camel.tc.cmq.meta;

// done
public class BrokerAcquireMetaRequest {

    private String hostname;
    private int port;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "BrokerAcquireMetaRequest{" +
                "hostname='" + hostname + '\'' +
                ", port=" + port +
                '}';
    }
}
