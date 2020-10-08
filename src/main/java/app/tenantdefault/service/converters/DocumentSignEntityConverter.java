package app.tenantdefault.service.converters;

import app.tenantdefault.models.DocumentSignEntity;
import app.tenantdefault.models.RetrieveDocumentKey;
import org.bson.Document;
import org.bson.types.Binary;
import org.springframework.stereotype.Component;

@Component
public class DocumentSignEntityConverter
        implements IEntityConverter<DocumentSignEntity> {

    @Override
    public DocumentSignEntity convert(Document doc) {
        DocumentSignEntity entity = convertExceptFileData(doc);
        Binary signData = null;
        if (doc.containsKey("binary")) {
            Object o2 = doc.get("binary");
            if (o2 instanceof Document) {
                Document o1 = (Document) o2;
                if (o1.containsKey("data")) {
                    if (o1.get("data") instanceof Binary) {
                        signData = (Binary) o1.get("data");
                    }
                }
            }
        }
        entity.setBinary(signData);
        return entity;
    }

    @Override
    public DocumentSignEntity convertExceptFileData(Document doc) {
        DocumentSignEntity entity = new DocumentSignEntity();
        entity.setWho(
                RetrieveDocumentKey
                        .getPrimitiveFrom(doc, "who", String.class)
        );
        entity.setWhen(
                RetrieveDocumentKey
                        .getPrimitiveFrom(doc, "when", String.class)
        );
        entity.setFileName(
                RetrieveDocumentKey
                        .getPrimitiveFrom(doc, "fileName", String.class)
        );
        entity.setExtension(
                RetrieveDocumentKey
                        .getPrimitiveFrom(doc, "extension", String.class)
        );
        return entity;

    }
}
