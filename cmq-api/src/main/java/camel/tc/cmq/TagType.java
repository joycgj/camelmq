package camel.tc.cmq;

import java.util.HashMap;
import java.util.Map;

// done
public enum TagType {
    NO_TAG(0), OR(1), AND(2);

    private int code;

    TagType(int code) {
        this.code = code;
    }

    private final static Map<Integer, TagType> MAP = new HashMap<>();

    static {
        for (TagType tagType : TagType.values()) {
            MAP.put(tagType.getCode(), tagType);
        }
    }

    public static TagType of(final int code) {
        final TagType tagType = MAP.get(code);
        if (tagType != null) {
            return tagType;
        }
        return TagType.NO_TAG;
    }

    public int getCode() {
        return code;
    }
}
