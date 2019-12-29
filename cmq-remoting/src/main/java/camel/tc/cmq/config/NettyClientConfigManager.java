package camel.tc.cmq.config;

import camel.tc.cmq.netty.NettyClientConfig;

// done
public class NettyClientConfigManager {

    private static final NettyClientConfigManager config = new NettyClientConfigManager();

    public static NettyClientConfigManager get() {
        return config;
    }

    private volatile NettyClientConfig clientConfig = new NettyClientConfig();

    private NettyClientConfigManager() {
    }

    public NettyClientConfig getDefaultClientConfig() {
        return clientConfig;
    }
}
