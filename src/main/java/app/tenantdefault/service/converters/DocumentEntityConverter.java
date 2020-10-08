package app.tenantdefault.service.converters;

import app.tenantdefault.models.DocumentEntity;
import app.tenantdefault.models.DocumentSignEntity;
import app.tenantdefault.models.RetrieveDocumentKey;
import java.util.ArrayList;
import org.bson.Document;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentEntityConverter implements IEntityConverter<DocumentEntity> {

    @Autowired
    private IEntityConverter<DocumentSignEntity> signEntityConverter;

    public DocumentEntity convert(Document doc) {
        DocumentEntity entity = convertExceptFileData(doc);
        Binary documentData = null;
        if (doc.containsKey("document")) {
            Object o2 = doc.get("document");
            if (o2 instanceof Document) {
                Document o1 = (Document) o2;
                if (o1.get("data") instanceof Binary) {
                    documentData = (Binary) o1.get("data");
                }
            }
        }
        entity.setDocument(documentData);
        Binary signData = null;
        if (doc.containsKey("sign")) {
            Object o2 = doc.get("sign");
            if (o2 instanceof Document) {
                Document o1 = (Document) o2;
                if (o1.containsKey("data")) {
                    if (o1.get("data") instanceof Binary) {
                        signData = (Binary) o1.get("data");
                    }
                }
            }
            if (o2 instanceof ArrayList) {
                ArrayList<Document> list =
                        (ArrayList<Document>) doc.get("sign");
                for (Document document : list) {
                    DocumentSignEntity sign = signEntityConverter.convert(document);
                    entity.addSign(sign);
                }
            }
        }
        return entity;
    }

    public DocumentEntity convertExceptFileData(Document doc) {
        DocumentEntity entity = new DocumentEntity();
        entity.setName(
                RetrieveDocumentKey
                        .getPrimitiveFrom(doc, "name", String.class)
        );
        entity.setId(
                RetrieveDocumentKey
                        .getPrimitiveFrom(doc, "_id", String.class)
        );
        entity.setExtension(
                RetrieveDocumentKey
                        .getPrimitiveFrom(doc, "extension", String.class)
        );
        entity.setTenantId(
                RetrieveDocumentKey
                        .getPrimitiveFrom(doc, "tenantId", String.class)
        );
        entity.setSize(
                RetrieveDocumentKey
                        .getPrimitiveFrom(doc, "size", Long.class)
        );
        return entity;
    }

}
