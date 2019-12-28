package camel.tc.cmq;

import java.util.Date;
import java.util.concurrent.TimeUnit;

// done
/**
 * <p/>
 * qmq会根据该异常里的时间进行重试间隔控制
 */
public class NeedRetryException extends RuntimeException {
    private final long next;

    public NeedRetryException(Date next, String message) {
        super(message);
        this.next = next.getTime();
    }

    public NeedRetryException(int next, TimeUnit unit, String message) {
        super(message);
        this.next = System.currentTimeMillis() + unit.toMillis(next);
    }

    /**
     * WARNING WARNING
     * 使用该构造函数构造的异常会立即重试
     *
     * @param message
     */
    public NeedRetryException(String message) {
        this(new Date(), message);
    }

    public long getNext() {
        return next;
    }
}
