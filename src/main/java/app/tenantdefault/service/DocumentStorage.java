package app.tenantdefault.service;

import app.configuration.spring.SpringMvcConfiguration;
import app.tenantdefault.models.DocumentEntity;
import app.tenantdefault.models.DocumentSignEntity;
import app.tenantdefault.service.converters.IEntityConverter;
import com.mongodb.client.MongoCollection;
import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.UpdateOperations;
import javax.annotation.PostConstruct;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentStorage implements IDocumentStorage {

    private static final BsonDocument projection;

    static {
        projection = new BsonDocument();
        projection.append(
                "document",
                new BsonBoolean(false)
        );
        projection.append(
                "sign",
                new BsonDocument(
                        "data",
                        new BsonBoolean(false)
                )
        );
    }

    @Autowired
    private IEntityConverter<DocumentEntity> docEntityConverter;

    @Autowired
    private Datastore datastore;

    private Long maxSizeUpload =
            SpringMvcConfiguration.MAX_SIZE_UPLOAD;

    private MongoCollection<Document> docCollection;

    @PostConstruct
    public void initAfterContruct() {
        docCollection = datastore
                .getDatabase()
                .getCollection(DocumentEntity.COLLECTION_NAME);
    }

    @Override
    public void save(DocumentEntity entity) {
        if (entity.getSize() > maxSizeUpload) {
            return;
        }
        datastore.save(entity);
    }

    @Override
    public DocumentEntity find(String uuid) {
        Document document = findByUuid(uuid);
        return docEntityConverter.convert(document);
    }

    @Override
    public DocumentEntity findForJson(String uuid) {
        BsonDocument filter = new BsonDocument();
        filter.append("_id", new BsonString(uuid));
        Document document = docCollection.find(filter).projection(projection).first();
        return docEntityConverter.convert(document);
    }

    @Override
    public void addSign(String uuid, DocumentSignEntity signing) {
        UpdateOperations<DocumentEntity> op = datastore
                .createUpdateOperations(
                        DocumentEntity.class
                );
        op.push("sign", signing);
        Query<DocumentEntity> query = findUuidQuery(uuid);
        datastore.update(query, op);
    }

    private Document findByUuid(String uuid) {
        BsonDocument filter = new BsonDocument();
        filter.append("_id", new BsonString(uuid));
        Document document = docCollection.find(filter).first();
        return document;
    }

    private Query<DocumentEntity> findUuidQuery(String uuid) {
        return datastore
                .createQuery(DocumentEntity.class)
                .field("_id").equal(uuid);
    }
}
