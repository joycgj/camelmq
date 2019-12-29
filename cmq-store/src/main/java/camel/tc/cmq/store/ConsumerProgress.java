package camel.tc.cmq.store;

// done
/**
 * 表示一个消费者的消费进度。
 * pull: 当前已经拉取了多少消息
 * ack: 当前已经Ack了多少消息
 */
public class ConsumerProgress {

    private final String subject;
    private final String group;
    private final String consumerId;

    private long pull;
    private long ack;

    public ConsumerProgress(String subject, String group, String consumerId, long pull, long ack) {
        this.subject = subject;
        this.group = group;
        this.consumerId = consumerId;
        this.pull = pull;
        this.ack = ack;
    }

    public ConsumerProgress(ConsumerProgress progress) {
        this.subject = progress.getSubject();
        this.group = progress.getGroup();
        this.consumerId = progress.getConsumerId();
        this.pull = progress.getPull();
        this.ack = progress.getAck();
    }

    public String getSubject() {
        return subject;
    }

    public String getGroup() {
        return group;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public long getPull() {
        return pull;
    }

    public void setPull(long pull) {
        this.pull = pull;
    }

    public long getAck() {
        return ack;
    }

    public void setAck(long ack) {
        this.ack = ack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsumerProgress that = (ConsumerProgress) o;

        if (!subject.equals(that.subject)) return false;
        if (!group.equals(that.group)) return false;
        return consumerId.equals(that.consumerId);
    }

    @Override
    public int hashCode() {
        int result = subject.hashCode();
        result = 31 * result + group.hashCode();
        result = 31 * result + consumerId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ConsumeProgress{" +
                "subject='" + subject + '\'' +
                ", group='" + group + '\'' +
                ", consumerId='" + consumerId + '\'' +
                ", pull=" + pull +
                ", ack=" + ack +
                '}';
    }
}
