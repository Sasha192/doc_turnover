package app.tenantconfiguration;

import app.controllers.dto.TenantDto;
import app.tenantconfiguration.interfaces.IFlywayTenantService;
import app.tenantconfiguration.interfaces.ITenantCreatorService;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import dev.morphia.Datastore;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service("tenant_default_creator")
public class TenantCreatorService implements ITenantCreatorService {

    @Autowired
    @Qualifier("flyway_default_service")
    private IFlywayTenantService<String> flywayTenantService;

    @Autowired
    private Datastore datastore;

    @Autowired
    private TenantConnectionProvider provider;

    private TimeBasedGenerator generator;

    {
        generator = Generators.timeBasedGenerator();
    }

    public String create()
            throws SQLException {
        String tenantId = generator.generate()
                .toString()
                .replace("-", "");
        DataSource dataSource = provider
                .createDataSourceAndDatabase(tenantId);
        provider.addTenantConnectionProvider(tenantId, dataSource);
        flywayTenantService.init(dataSource, tenantId);
        return tenantId;
    }

    private int abs(int hash) {
        return Math.abs(hash);
    }

    private String substring(String str, int end) {
        if (str.length() < end) {
            return str;
        }
        return str.substring(0, end);
    }

}
