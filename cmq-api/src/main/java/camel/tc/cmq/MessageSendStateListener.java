package camel.tc.cmq;

/**
 * 消息的发送状态监听器
 */
public interface MessageSendStateListener {
    /**
     * 消息成功发送后触发
     *
     * @param message
     */
    void onSuccess(Message message);

    /**
     * 消息发送失败时触发
     *
     * @param message
     */
    void onFailed(Message message);
}
