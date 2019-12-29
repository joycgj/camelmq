package camel.tc.cmq.jdbc;

import java.util.ServiceLoader;

import javax.sql.DataSource;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import camel.tc.cmq.configuration.DynamicConfig;
import camel.tc.cmq.configuration.DynamicConfigLoader;

import org.springframework.jdbc.core.JdbcTemplate;

// done
public class JdbcTemplateHolder {

    private static final Supplier<DataSource> DS_SUPPLIER = Suppliers.memoize(JdbcTemplateHolder::createDataSource);
    private static final Supplier<JdbcTemplate> SUPPLIER = Suppliers.memoize(JdbcTemplateHolder::createJdbcTemplate);

    private static JdbcTemplate createJdbcTemplate() {
        return new JdbcTemplate(DS_SUPPLIER.get());
    }

    private static DataSource createDataSource() {
        final DynamicConfig config = DynamicConfigLoader.load("datasource.properties");
        ServiceLoader<DataSourceFactory> factories = ServiceLoader.load(DataSourceFactory.class);
        for (DataSourceFactory factory : factories) {
            return factory.createDataSource(config);
        }

        return new DefaultDataSourceFactory().createDataSource(config);
    }

    public static JdbcTemplate getOrCreate() {
        return SUPPLIER.get();
    }
}
