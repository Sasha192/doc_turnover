package app.tenantdefault.models;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import java.util.LinkedList;
import java.util.List;
import org.bson.types.Binary;

@Entity("documents")
public class DocumentEntity extends CollectionNameHolder {

    @Id
    private String id;

    private String extension;

    private String name;

    private String tenantId;

    private long size;

    private Binary document;

    private List<DocumentSignEntity> sign;

    public DocumentEntity() {
        super("documents");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Binary getDocument() {
        return document;
    }

    public void setDocument(Binary document) {
        this.document = document;
    }

    public List<DocumentSignEntity> getSign() {
        return sign;
    }

    public void addSign(DocumentSignEntity signature) {
        if (this.sign == null) {
            this.sign = new LinkedList<>();
        }
        this.sign.add(signature);
    }
}
