package app.tenantdefault.service;

import app.tenantdefault.models.TenantInfoEntity;
import java.util.Collection;
import org.bson.Document;

public interface ITenantService {

    Collection<Document> findMyTenants(Collection<String> uuids);

    TenantInfoEntity findById(String tenantId);

    Collection<String> findTenants();

    void remove(String tenantId);

    void update(TenantInfoEntity entity);
}
