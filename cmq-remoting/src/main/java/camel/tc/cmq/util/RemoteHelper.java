package camel.tc.cmq.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class RemoteHelper {

    private static final Logger LOG = LoggerFactory.getLogger(RemoteHelper.class);

    public static String parseChannelRemoteAddress(final Channel channel) {
        if (channel == null) {
            return "";
        }

        final SocketAddress remote = channel.remoteAddress();
        final String address = remote != null ? remote.toString() : "";

        final int index = address.lastIndexOf("/");
        if (index < 0) {
            return address;
        } else {
            return address.substring(index + 1);
        }
    }

    public static String parseSocketAddressAddress(SocketAddress address) {
        if (address == null) {
            return "";
        }

        final String addr = address.toString();
        if (addr.isEmpty()) {
            return "";
        } else {
            return addr.substring(1);
        }
    }

    public static SocketAddress string2SocketAddress(final String address) {
        final String[] s = address.split(":");
        return new InetSocketAddress(s[0], Integer.parseInt(s[1]));
    }

    public static void closeChannel(Channel channel, final boolean enableLog) {
        final String remoteAddr = RemoteHelper.parseChannelRemoteAddress(channel);
        channel.close().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                if (enableLog) {
                    LOG.info("close channel result: {}. isClosed={}", remoteAddr, future.isSuccess());
                }
            }
        });
    }
}
