package camel.tc.cmq;

// done
public interface MessageStore {

    /**
     * 将消息持久化到本地存储
     *
     * @param message
     */
    long insertNew(ProduceMessage message);

    /**
     * 发送完成，删除消息
     *
     * @param message
     */
    void finish(ProduceMessage message);

    /**
     * 消息被server拦截
     *
     * @param message
     */
    void block(ProduceMessage message);

    /**
     * 事务开始
     */
    void beginTransaction();

    /**
     * 事务结束
     */
    void endTransaction();
}

