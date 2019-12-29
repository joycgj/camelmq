package camel.tc.cmq.utils;

import java.util.Arrays;
import java.util.List;

// done
public class ListUtils {

    public static boolean contains(List<byte[]> source, byte[] target) {
        for (byte[] bytes : source) {
            if (Arrays.equals(bytes, target)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsAll(List<byte[]> source, List<byte[]> target) {
        if (source.size() < target.size()) return false;
        for (byte[] bytes : target)
            if (!contains(source, bytes)) {
                return false;
            }
        return true;
    }

    public static boolean intersection(List<byte[]> source, List<byte[]> target) {
        for (byte[] bytes : target) {
            if (contains(source, bytes)) {
                return true;
            }
        }
        return false;
    }
}
