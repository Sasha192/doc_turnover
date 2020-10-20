package app.tenantdefault.service;

import app.tenantdefault.models.TenantInfoEntity;
import java.util.Collection;
import org.bson.BsonDocument;

public interface ITenantService {

    Collection<TenantInfoEntity> findPageableOpen(int page);

    Collection<TenantInfoEntity> findPageableFilter(int page, BsonDocument filter);

    Collection<TenantInfoEntity> findMyTenants(Collection<String> uuids);

    TenantInfoEntity findById(String tenantId);
}
