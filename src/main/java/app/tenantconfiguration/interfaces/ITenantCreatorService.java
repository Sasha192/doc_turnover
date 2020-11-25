package app.tenantconfiguration.interfaces;

import java.sql.SQLException;

public interface ITenantCreatorService {

    String create() throws SQLException;

    void remove(String id) throws SQLException;
}
