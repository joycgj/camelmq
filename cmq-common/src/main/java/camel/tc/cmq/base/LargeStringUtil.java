package camel.tc.cmq.base;

// done
class LargeStringUtil {

    private static final int _32K = (32 * 1024) / 4;

    static void setLargeString(BaseMessage msg, String key, String data) {
        int len = data.length();
        if (len <= _32K) {
            msg.setProperty(key, data);
            return;
        }

        msg.isBigMessage = true;
        int partIdx = 0;
        for (int remain = len; remain > 0; remain -= _32K) {
            final int beginIdx = partIdx * _32K;
            final int endIdx = beginIdx + Math.min(_32K, remain);
            final String part = data.substring(beginIdx, endIdx);
            msg.setProperty(buildPartKey(key, partIdx), part);
            partIdx += 1;
        }
    }

    static String getLargeString(BaseMessage msg, String key) {
        String small = msg.getStringProperty(key);
        if (small != null) return small;

        StringBuilder result = new StringBuilder();
        int partIdx = 0;
        while (true) {
            String part = msg.getStringProperty(buildPartKey(key, partIdx));
            if (part == null) {
                break;
            }
            partIdx += 1;
            result.append(part);
        }
        return result.toString();
    }

    private static String buildPartKey(String key, int idx) {
        return key + "#part" + idx;
    }
}
