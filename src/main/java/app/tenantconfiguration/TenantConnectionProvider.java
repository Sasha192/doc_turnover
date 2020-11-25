package app.tenantconfiguration;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.core.env.Environment;

public class TenantConnectionProvider
        extends AbstractMultiTenantConnectionProvider {

    private final Map<String, ConnectionProvider> connectionProviderMap =
            new ConcurrentHashMap<>();

    private BasicDataSource defaultDataSource;

    private BasicDataSource phantomDataSource;

    private Environment env;

    private Properties properties;

    public TenantConnectionProvider(DataSource defaultDs,
                                    DataSource phantomDs,
                                    Collection<String> tenants,
                                    Environment environment,
                                    Properties properties) {
        this.env = environment;
        this.properties = properties;
        if (defaultDs instanceof BasicDataSource
                && phantomDs instanceof BasicDataSource) {
            this.defaultDataSource = (BasicDataSource) defaultDs;
            this.phantomDataSource = (BasicDataSource) phantomDs;
            addTenantConnectionProvider(TenantContext.DEFAULT_TENANT_IDENTIFIER);
            addTenantConnectionProvider(TenantContext.PHANTOM_TENANT_IDENTIFIER);
            for (String id : tenants) {
                addTenantConnectionProvider(id);
            }
        } else {
            throw new UnsatisfiedDependencyException(TenantConnectionProvider.class.getName(),
                    BasicDataSource.class.getName(),
                    defaultDs.getClass().getName(),
                    "");
        }
    }

    @Override
    protected ConnectionProvider getAnyConnectionProvider() {
        return connectionProviderMap.get(
                TenantContext.DEFAULT_TENANT_IDENTIFIER
        );
    }

    @Override
    protected ConnectionProvider selectConnectionProvider(String tenantIdentifier) {
        return connectionProviderMap.get(tenantIdentifier);
    }

    private void addTenantConnectionProvider(String tenantId,
                                             DataSource tenantDataSource,
                                             Properties properties) {
        DatasourceConnectionProviderImpl connectionProvider =
                new DatasourceConnectionProviderImpl();
        connectionProvider.setDataSource(tenantDataSource);
        connectionProvider.configure(properties);
        connectionProviderMap.put(
                tenantId,
                connectionProvider
        );
    }

    public void addTenantConnectionProvider(String tenantId, DataSource tenantDataSource) {
        Properties properties = properties();
        properties.put(
                org.hibernate.cfg.Environment.DATASOURCE,
                dataSourceProxyType(tenantDataSource)
        );
        addTenantConnectionProvider(tenantId, tenantDataSource, properties);
    }

    public DataSource addTenantConnectionProvider(String tenantId) {
        BasicDataSource tenantDataSource = createDataSource(tenantId);
        Properties properties = properties();
        properties.put(
                org.hibernate.cfg.Environment.DATASOURCE,
                dataSourceProxyType(tenantDataSource)
        );
        addTenantConnectionProvider(tenantId, tenantDataSource, properties);
        return tenantDataSource;
    }

    public BasicDataSource createDataSource(String tenantId) {
        BasicDataSource tenantDataSource = null;
        if (tenantId.equals(TenantContext.DEFAULT_TENANT_IDENTIFIER)) {
            tenantDataSource = defaultDataSource;
        } else if (tenantId.equals(TenantContext.PHANTOM_TENANT_IDENTIFIER)) {
            tenantDataSource = phantomDataSource;
        } else {
            String url = env.getProperty("db.abstract_url");
            url = url.replaceAll("\\{db_name\\}", tenantId);
            tenantDataSource = new BasicDataSource();
            tenantDataSource.setDriverClassName(defaultDataSource.getDriverClassName());
            tenantDataSource.setUrl(url);
            tenantDataSource.setUsername(defaultDataSource.getUsername());
            tenantDataSource.setPassword(defaultDataSource.getPassword());
        }
        return tenantDataSource;
    }

    private ProxyDataSource dataSourceProxyType(DataSource dataSource) {
        return ProxyDataSourceBuilder
                .create(dataSource)
                .build();
    }

    private ConnectionProvider getProvider(String key) {
        if (connectionProviderMap.containsKey(key)) {
            return connectionProviderMap.get(key);
        }
        return null;
    }

    private Properties properties() {
        return (Properties) properties.clone();
    }

    public boolean tenantExist(String id) {
        return connectionProviderMap.containsKey(id);
    }

    public DataSource createDataSourceAndDatabase(String tenantId) throws SQLException {
        getAnyConnectionProvider().getConnection()
                .createStatement()
                .execute("CREATE SCHEMA " + tenantId);
        return createDataSource(tenantId);
    }

    public void remove(String id)
            throws SQLException {
        String query = String.format("DROP SCHEMA IF EXISTS `%s`", id);
        getAnyConnectionProvider()
                .getConnection()
                .createStatement()
                .execute(query);
        connectionProviderMap.remove(id);
    }
}
