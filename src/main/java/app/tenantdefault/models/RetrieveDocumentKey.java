package app.tenantdefault.models;

import org.bson.Document;

public class RetrieveDocumentKey {

    public static <T> T getPrimitiveFrom(Document doc, String key, Class<T> clazz) {
        if (doc.containsKey(key)) {
            return (T) doc.get(key);
        }
        return null;
    }

}
