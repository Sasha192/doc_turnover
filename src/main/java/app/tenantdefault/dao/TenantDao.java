package app.tenantdefault.dao;

import app.tenantdefault.models.TenantInfoEntity;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import dev.morphia.Datastore;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TenantDao implements ITenantDao {

    private static final int pageSize = 30;

    private static final int bastSize = 15;

    private static final BsonDocument projection;

    static {
        projection = new BsonDocument();
        projection.append("ownerId", new BsonBoolean(false));
        projection.append("tariff", new BsonBoolean(false));
    }

    @Autowired
    private Datastore datastore;

    private MongoCollection tenantCol;

    private Class<TenantInfoEntity> clazz = TenantInfoEntity.class;

    @PostConstruct
    public void init() {
        tenantCol = datastore
                .getDatabase()
                .getCollection(TenantInfoEntity.COLLECTION_NAME);
    }

    @Override
    public Collection<TenantInfoEntity> findPageableOpen(int page) {
        BsonDocument filter = new BsonDocument();
        filter.append("privateAccount", new BsonBoolean(false));
        return findPageableFilter(page, filter);
    }

    @Override
    public Collection<TenantInfoEntity> findPageableFilter(int page, BsonDocument filter) {
        int ski = (page - 1) * pageSize;
        MongoCursor cursor = tenantCol.find(filter)
                .projection(projection)
                .limit(pageSize)
                .skip(ski)
                .iterator();
        List<TenantInfoEntity> tenants = new LinkedList<>();
        int counter = 0;
        Object o = null;
        while (cursor.hasNext() && counter++ < pageSize) {
            if ((o = cursor.next()) instanceof TenantInfoEntity) {
                tenants.add((TenantInfoEntity) o);
            }
        }
        return tenants;
    }

    @Override
    public Collection<TenantInfoEntity> findMyTenants(Collection<String> uuids) {
        BsonDocument filter = new BsonDocument();
        BsonArray array = new BsonArray();
        for (String id : uuids) {
            array.add(new BsonString(id));
        }
        filter.append("_id", array);
        List<TenantInfoEntity> tenants = new LinkedList<>();
        return tenantCol.find(filter).projection(projection)
                .into(tenants);
    }

    @Override
    public TenantInfoEntity findById(String tenantId) {
        BsonDocument filter = new BsonDocument();
        filter.append("_id", new BsonString(tenantId));
        return (TenantInfoEntity) tenantCol.find(filter)
                .first();
    }
}
