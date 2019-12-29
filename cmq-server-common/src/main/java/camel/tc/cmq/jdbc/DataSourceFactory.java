package camel.tc.cmq.jdbc;

import javax.sql.DataSource;

import camel.tc.cmq.configuration.DynamicConfig;

// done
public interface DataSourceFactory {

	DataSource createDataSource(DynamicConfig config);
}
