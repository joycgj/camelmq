package camel.tc.cmq.utils;

// done
public final class ObjectUtils {
    public static <T> T defaultIfNull(T cond, T defaultValue) {
        if (cond == null) {
            return defaultValue;
        } else {
            return cond;
        }
    }
}
