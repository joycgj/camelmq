package camel.tc.cmq.configuration;

// done
public interface DynamicConfigFactory {

    DynamicConfig create(String name, boolean failOnNotExist);
}
