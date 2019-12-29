package camel.tc.cmq.protocol.producer;

public class SendResult {

    public static final SendResult OK = new SendResult(MessageProducerCode.SUCCESS, "");

    private final int code;
    private final String remark;

    public SendResult(int code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public int getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }

    @Override
    public String toString() {
        return "SendResult{" +
                "code=" + code +
                ", remark='" + remark + '\'' +
                '}';
    }
}
