package camel.tc.cmq.store;

import com.google.common.collect.ImmutableMap;
import camel.tc.cmq.store.action.ForeverOfflineActionReaderWriter;
import camel.tc.cmq.store.action.PullActionReaderWriter;
import camel.tc.cmq.store.action.RangeAckActionReaderWriter;

import java.util.HashMap;
import java.util.Map;

// done
public enum ActionType {

    PULL((byte) 0, new PullActionReaderWriter()),
    RANGE_ACK((byte) 1, new RangeAckActionReaderWriter()),
    FOREVER_OFFLINE((byte) 2, new ForeverOfflineActionReaderWriter());

    private static final ImmutableMap<Byte, ActionType> INSTANCES;

    static {
        final Map<Byte, ActionType> instances = new HashMap<>();
        for (final ActionType t : values()) {
            instances.put(t.getCode(), t);
        }
        INSTANCES = ImmutableMap.copyOf(instances);
    }

    private final byte code;
    private final ActionReaderWriter readerWriter;

    ActionType(final byte code, final ActionReaderWriter readerWriter) {
        this.code = code;
        this.readerWriter = readerWriter;
    }

    public static ActionType fromCode(final byte code) {
        if (INSTANCES.containsKey(code)) {
            return INSTANCES.get(code);
        }

        throw new RuntimeException("unknown action type code " + code);
    }

    public byte getCode() {
        return code;
    }

    public ActionReaderWriter getReaderWriter() {
        return readerWriter;
    }
}
