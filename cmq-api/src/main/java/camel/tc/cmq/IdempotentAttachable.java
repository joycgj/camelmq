package camel.tc.cmq;

// done

/**
 * <p/>
 * 通过这个接口可以给MessageListener添加幂等检查的功能
 */
public interface IdempotentAttachable {
    IdempotentChecker getIdempotentChecker();
}