package app.tenantdefault.models;

public abstract class CollectionNameHolder {

    private transient String collectionName;

    public CollectionNameHolder(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCollectionName() {
        return collectionName;
    }
}

