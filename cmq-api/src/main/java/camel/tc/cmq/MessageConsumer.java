package camel.tc.cmq;

import java.util.concurrent.Executor;

// done
public interface MessageConsumer {

    /**
     * 注册消息处理程序
     *
     * @param subject       订阅的消息主题
     * @param consumerGroup consumer分组，用于consumer的负载均衡(broker只会给每个consumer group发送一条消息)。
     *                      如果想每个consumer进程都收到消息(广播模式)，只需要给group参数传空字符串即可。
     * @param listener      消息处理程序
     * @param executor      消息处理线程池
     * @return 返回的ListenerHolder, 表示注册关系
     */
    ListenerHolder addListener(String subject, String consumerGroup, MessageListener listener, Executor executor);

    /**
     * 注册消息处理程序
     *
     * @param subject       订阅的消息主题
     * @param consumerGroup consumer分组，用于consumer的负载均衡(broker只会给每个consumer group发送一条消息)。
     *                      如果想每个consumer进程都收到消息(广播模式)，只需要给group参数传空字符串即可。
     * @param listener      消息处理程序
     * @param executor      消息处理线程池
     * @return 返回的ListenerHolder, 表示注册关系
     */
    ListenerHolder addListener(String subject, String consumerGroup, MessageListener listener, Executor executor, SubscribeParam subscribeParam);

    /**
     * @param group       nullOrEmpty时，是广播订阅
     * @param isBroadcast 等于true时，忽略group参数，广播订阅；等于false时，group不能是nullOrEmpty
     */
    PullConsumer getOrCreatePullConsumer(String subject, String group, boolean isBroadcast);
}
