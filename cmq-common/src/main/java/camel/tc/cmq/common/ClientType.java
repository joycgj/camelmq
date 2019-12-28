package camel.tc.cmq.common;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

// done
public enum ClientType {

    PRODUCER(1),
    CONSUMER(2),
    OTHER(3),
    DELAY_PRODUCER(4);

    private static final ImmutableMap<Integer, ClientType> INSTANCES;

    static {
        final Map<Integer, ClientType> result = new HashMap<>();
        for (final ClientType type : values()) {
            result.put(type.getCode(), type);
        }
        INSTANCES = ImmutableMap.copyOf(result);
    }

    private int code;

    ClientType(int code) {
        this.code = code;
    }

    public static ClientType of(final int code) {
        ClientType type = INSTANCES.get(code);
        return type == null ? OTHER : type;
    }

    public int getCode() {
        return code;
    }

    public boolean isProducer() {
        return code == PRODUCER.code || code == DELAY_PRODUCER.code;
    }

    public boolean isConsumer() {
        return code == CONSUMER.code;
    }
}
