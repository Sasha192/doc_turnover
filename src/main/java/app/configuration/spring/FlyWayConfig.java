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
        Flyway flyway = Flyway.configure()
                .locations(defaultMigrations)
                .dataSource(dataSource)
                .schemas(TenantContext.DEFAULT_TENANT_IDENTIFIER)
                .baselineDescription(baseLine)
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        return flyway;
    }
}
