package app.tenantconfiguration;

import app.tenantconfiguration.interfaces.IFlywayTenantService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Qualifier("flyway_default_service")
public class FlywayTenantService implements IFlywayTenantService<String> {

    private String migrationPath;

    private Flyway defaultFlyway;

    private String migrationBaseLine;

    private Map<String, Flyway> map =
            new ConcurrentHashMap<>();

    @Autowired
    public FlywayTenantService(Environment env,
                               @Qualifier("default_flyway") Flyway flyway) {
        this.migrationPath = env.getProperty("flyway.tenant.migration.path");
        this.migrationBaseLine = env.getProperty("flyway.tenant.migration.baseline");
        this.defaultFlyway = flyway;
    }

    @Override
    public void init(DataSource dataSource, String tenant) {
        Flyway flyway = Flyway.configure()
                .locations(migrationPath)
                .dataSource(dataSource)
                .schemas(tenant)
                .baselineDescription(migrationBaseLine)
                .load();
        flyway.migrate();
        map.put(tenant, flyway);
    }

    @Override
    public void connect(DataSource dataSource, String tenant) {
        Flyway flyway = Flyway.configure()
                .locations(migrationPath)
                .dataSource(dataSource)
                .schemas(tenant)
                .baselineDescription(migrationBaseLine)
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        map.put(tenant, flyway);
    }

    @Override
    @NotNull
    public Flyway getFlyway(String id) {
        return map.get(id);
    }

    @Override
    public void migrate(String id) {
        Flyway flyway = map.get(id);
        if (flyway != null) {
            flyway.migrate();
        }
    }

    @Override
    public void migrateAll() {
        map.forEach((key, flyway) -> {
            flyway.migrate();
        });
    }

    @Override
    public void migrateDefault() {
        this.defaultFlyway.migrate();
    }
}
