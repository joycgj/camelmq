package camel.tc.cmq.batch;

import java.util.List;

// done
public interface Processor<Item> {

    void process(List<Item> items);
}
