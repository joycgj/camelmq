package camel.tc.cmq;

import java.util.Map;

// done
public interface Filter {

    /**
     * 在listener.onMessage之前执行
     *
     * @param message       处理的消息，建议不要修改消息内容
     * @param filterContext 可以在这里保存一些上下文
     * @return 如果返回true则filter链继续往下执行，只要任一filter返回false，则后续的
     * filter链不会执行，并且listener.onMessage也不会执行
     */
    boolean preOnMessage(Message message, Map<String, Object> filterContext);

    /**
     * 在listener.onMessage之后执行，可以做一些资源清理工作
     *
     * @param message       处理的消息
     * @param e             filter链和listener.onMessage抛出的异常
     * @param filterContext 上下文
     */
    void postOnMessage(Message message, Throwable e, Map<String, Object> filterContext);
}
