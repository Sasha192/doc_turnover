package app.tenantdefault.dao;

import app.tenantdefault.models.TenantInfoEntity;
import java.util.Collection;
import java.util.List;

import org.bson.BsonDocument;

public interface ITenantDao {
    Collection<TenantInfoEntity> findPageableOpen(int page);

    Collection<TenantInfoEntity> findPageableFilter(int page, BsonDocument filter);

    Collection<TenantInfoEntity> findMyTenants(Collection<String> uuids);

    TenantInfoEntity findById(String tenantId);

    Collection<String> findTenants();

    void remove(String tenantId);
}
