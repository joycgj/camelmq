package camel.tc.cmq.configuration;

import camel.tc.cmq.configuration.local.LocalDynamicConfigFactory;

import java.util.ServiceLoader;

// done
public final class DynamicConfigLoader {
    private static final DynamicConfigFactory FACTORY;

    static {
        ServiceLoader<DynamicConfigFactory> factories = ServiceLoader.load(DynamicConfigFactory.class);
        DynamicConfigFactory instance = null;
        for (DynamicConfigFactory factory : factories) {
            instance = factory;
            break;
        }

        if (instance == null) {
            instance = new LocalDynamicConfigFactory();
        }

        FACTORY = instance;
    }

    private DynamicConfigLoader() {
    }

    public static DynamicConfig load(final String name) {
        return load(name, true);
    }

    public static DynamicConfig load(final String name, final boolean failOnNotExist) {
        return FACTORY.create(name, failOnNotExist);
    }
}
