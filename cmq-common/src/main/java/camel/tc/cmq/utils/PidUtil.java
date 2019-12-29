package camel.tc.cmq.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

// done
public class PidUtil {

    public static int getPid() {
        try {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            String name = runtime.getName(); // format: "pid@hostname"
            return Integer.parseInt(name.substring(0, name.indexOf('@')));
        } catch (Throwable e) {
            return 0;
        }
    }
}
