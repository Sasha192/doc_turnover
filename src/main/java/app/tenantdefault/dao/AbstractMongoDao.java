package app.tenantdefault.dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import dev.morphia.Datastore;
import dev.morphia.Key;
import dev.morphia.query.Query;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractMongoDao<T extends Serializable, I> {

    private Datastore datastore;
    private Class<T> clazz;
    private int pageSize = 30;
    private FindIterable first;
    private FindIterable second;
    private MongoCollection collection;

    public AbstractMongoDao(Datastore datastore, Class<T> clazz, String collectionName) {
        this.datastore = datastore;
        this.clazz = clazz;
        this.collection = datastore
                .getDatabase()
                .getCollection(collectionName);
        first = collection
                .find()
                .skip(0)
                .limit(pageSize);
        second = collection
                .find()
                .skip(pageSize)
                .limit(pageSize);
    }

    public final void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        first = collection
                .find()
                .skip(0)
                .limit(pageSize);
        second = collection
                .find()
                .skip(pageSize)
                .limit(pageSize);
    }

    public Datastore getDatastore() {
        return datastore;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    /**
     * @TODO Mongo could return MongoDocument,
     * that will store sensitive data.
     */
    public List<Object> find(int page) {
        int skip = (page - 1) * pageSize;
        FindIterable p = null;
        if (page == 1) {
            p = first;
        } else {
            if (page == 2) {
                p = second;
            } else {
                p = collection.find().skip(skip).limit(pageSize);;
            }
        }
        List<Object> rez = new LinkedList<>();
        MongoCursor cursor = p.cursor();
        if (null != cursor) {
            for (int i = 0; i < pageSize && cursor.hasNext(); i++) {
                Object o = cursor.tryNext();
                rez.add(o);
            }
        }
        return rez;
    }

    public T find(I id) {
        Query<T> query = datastore.createQuery(clazz)
                .field("_id")
                .equal(id);
        return query.find().tryNext();
    }

    public void delete(T entity) {
        datastore.delete(entity);
    }

    public Key<T> save(T entity) {
        return datastore.save(entity);
    }
}
