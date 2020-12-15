package app.configuration.spring;

import app.tenantconfiguration.TenantContext;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class FlyWayConfig {

    @Autowired
    private Environment environment;

    @Bean("default_flyway")
    public Flyway flyway(@Qualifier("bcrew_default_data_source") DataSource dataSource) {
        String defaultMigrations = environment
                .getProperty("flyway.default_migrations.path");
        String baseLine = environment.getProperty("flyway.default_migrations.baseLine");
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations(defaultMigrations);
        flyway.setSchemas(TenantContext.DEFAULT_TENANT_IDENTIFIER);
        flyway.setBaselineDescription(baseLine);
        flyway.setBaselineOnMigrate(true);
        flyway.migrate();
        return flyway;
    }
}
