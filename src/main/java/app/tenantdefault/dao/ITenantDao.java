package app.tenantdefault.dao;

import app.tenantdefault.models.TenantInfoEntity;
import java.util.Collection;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.Document;

public interface ITenantDao {

    Collection<Document> findMyTenants(Collection<String> uuids);

    TenantInfoEntity findById(String tenantId);

    Collection<String> findTenants();

    void remove(String tenantId);

    void update(TenantInfoEntity entity);
}
