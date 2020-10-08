package app.tenantconfiguration.interfaces;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;

public interface IFlywayTenantService<I> {

    void init(DataSource dataSource, String tenant);

    void connect(DataSource dataSource, String tenant);

    Flyway getFlyway(I id) throws NullPointerException;

    void migrate(I id);

    void migrateAll();

    void migrateDefault();

    // @TODO
    //void repair(...);
}
