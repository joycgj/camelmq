package camel.tc.cmq.meta;

// done
public class BrokerAcquireMetaResponse {

    private String name;
    private BrokerRole role;
    private String master;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BrokerRole getRole() {
        return role;
    }

    public void setRole(BrokerRole role) {
        this.role = role;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    @Override
    public String toString() {
        return "BrokerAcquireMetaResponse{" +
                "name='" + name + '\'' +
                ", role=" + role +
                ", master='" + master + '\'' +
                '}';
    }
}
