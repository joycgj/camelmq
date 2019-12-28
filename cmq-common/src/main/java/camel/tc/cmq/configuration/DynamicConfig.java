package camel.tc.cmq.configuration;

import java.util.Map;

// done
public interface DynamicConfig {

    void addListener(Listener listener);

    String getString(String name);

    String getString(String name, String defaultValue);

    int getInt(String name);

    int getInt(String name, int defaultValue);

    long getLong(String name);

    long getLong(String name, long defaultValue);

    double getDouble(String name);

    double getDouble(String name, double defaultValue);

    boolean getBoolean(String name, boolean defaultValue);

    boolean exist(String name);

    Map<String, String> asMap();
}
