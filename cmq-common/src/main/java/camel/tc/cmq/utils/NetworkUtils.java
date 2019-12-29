package camel.tc.cmq.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

// done
public class NetworkUtils {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkUtils.class);

    public static String getLocalHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            LOG.error("get local hostname failed. return local ip instead.", e);
            return getLocalAddress();
        }
    }

    public static String getLocalAddress() {
        try {
            final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            final ArrayList<String> ipv4Result = new ArrayList<>();
            final ArrayList<String> ipv6Result = new ArrayList<>();
            while (interfaces.hasMoreElements()) {
                final NetworkInterface networkInterface = interfaces.nextElement();
                if (!networkInterface.isUp()) continue;
                if (networkInterface.isVirtual()) continue;

                final Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    final InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress()) {
                        if (address instanceof Inet6Address) {
                            ipv6Result.add(address.getHostAddress());
                        } else {
                            ipv4Result.add(address.getHostAddress());
                        }
                    }
                }
            }

            if (!ipv4Result.isEmpty()) {
                for (String ip : ipv4Result) {
                    if (ip.startsWith("127.0")) {
                        continue;
                    }

                    return ip;
                }

                return ipv4Result.get(ipv4Result.size() - 1);
            } else if (!ipv6Result.isEmpty()) {
                return ipv6Result.get(0);
            }

            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            LOG.error("get local address failed", e);
        }

        return null;
    }

    public static boolean isValid(String address) {
        try {
            final String[] s = address.split(":");
            new InetSocketAddress(s[0], Integer.parseInt(s[1]));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
