package camel.tc.cmq;

import java.util.List;

// done

/**
 * MessageListener如果实现了这个接口，则可以附加filter
 */
public interface FilterAttachable {
    List<Filter> filters();
}
