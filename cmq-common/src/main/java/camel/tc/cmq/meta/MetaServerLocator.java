package camel.tc.cmq.meta;

import com.google.common.base.Optional;
import com.google.common.io.CharStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import camel.tc.cmq.utils.NetworkUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// done
public class MetaServerLocator {

    private static final Logger LOG = LoggerFactory.getLogger(MetaServerLocator.class);

    private final String metaServerEndpoint;

    public MetaServerLocator(final String metaServerEndpoint) {
        this.metaServerEndpoint = metaServerEndpoint;
    }

    public Optional<String> queryEndpoint() {
        final String endpoint = request();
        if (endpoint == null || endpoint.length() == 0) {
            LOG.error("meta server address list is empty!");
            return Optional.absent();
        }

        if (NetworkUtils.isValid(endpoint)) {
            return Optional.of(endpoint);
        }
        return Optional.absent();
    }

    private String request() {
        InputStreamReader in = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) (new URL(metaServerEndpoint).openConnection());
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(500);
            connection.setDoInput(true);
            in = new InputStreamReader(connection.getInputStream());
            String content = CharStreams.toString(in);
            if (connection.getResponseCode() != 200) {
                return null;
            }
            return content.trim();
        } catch (IOException e) {
            if (connection == null || connection.getErrorStream() == null) {
                return null;
            }
            InputStreamReader errIn = null;
            try {
                errIn = new InputStreamReader(connection.getErrorStream());
                String error = CharStreams.toString(errIn);
                LOG.debug("read error stream {}", error);
            } catch (IOException e1) {
                LOG.debug("read error stream failed", e1);
            } finally {
                closeQuietly(errIn);
            }
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            closeQuietly(in);
        }
    }

    private void closeQuietly(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (Exception e) {
            LOG.debug("close failed");
        }
    }
}
