package camel.tc.cmq;

// done
public interface MessageProducer {

    /**
     * 在发送消息之前调用该接口生成消息，该接口会生成唯一消息id。这条消息的过期时间为默认的15分钟
     *
     * @param subject 要发送的消息的subject
     * @return 生成的消息
     */
    Message generateMessage(String subject);

    /**
     * 发送消息
     * 注意：在使用事务性消息时该方法仅将消息入库，当事务成功提交时才发送消息。只要事务提交，消息就会发送(即使入库失败)。
     * 在使用事务消息时，该方法会使用业务方配置的数据源操作qmq_produce数据库，请确保为业务datasource配置的数据库用户
     * 具有对qmq_produce数据库的CURD权限。
     * <p/>
     * 该方法须在generateMessage方法调用之后调用 @see generateMessage
     *
     * @param message
     * @throws RuntimeException 当消息体超过指定大小(60K)的时候会抛出RuntimeException异常
     *                          当消息过期时间设置非法的时候会抛出RuntimeException异常
     */
    void sendMessage(Message message);

    void sendMessage(Message message, MessageSendStateListener listener);
}
