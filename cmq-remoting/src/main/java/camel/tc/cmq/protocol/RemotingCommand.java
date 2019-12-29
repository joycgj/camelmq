package camel.tc.cmq.protocol;

// done
public class RemotingCommand extends Datagram {

    private long receiveTime;

    public RemotingCommandType getCommandType() {
        int bits = 1;
        int flag0 = this.header.getFlag() & bits;
        return RemotingCommandType.codeOf(flag0);
    }

    public boolean isOneWay() {
        int bits = 1 << 1;
        return (this.header.getFlag() & bits) == bits;
    }

    public int getOpaque() {
        return header.getOpaque();
    }

    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }
}
