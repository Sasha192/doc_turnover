package app.customtenant.models.basic;

import app.customtenant.models.abstr.IdentityBaseEntity;
import app.customtenant.models.serialization.ExcludeForBDocs;
import app.tenantdefault.models.DocumentEntity;
import app.utils.CustomAppDateTimeUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "brief_documents")
public class BriefDocument
        extends IdentityBaseEntity
        implements Serializable {

    @Column(name = "creation_date")
    private Date date = CustomAppDateTimeUtil.now();

    @Column(name = "file_name")
    private String name;

    @Column(name = "ext_name")
    private String extName;

    @Column(name = "path_uuid")
    private String uuid;

    @Column(name = "creation_time")
    private long time = System.currentTimeMillis();

    @Column(name = "signed")
    private boolean signed = false;

    @Column(name = "performer_id")
    private Long performerId;

    @ElementCollection
    @CollectionTable(name = "words_documents",
            joinColumns = @JoinColumn(name = "doc_id",
                    referencedColumnName = "id"))
    @Column(name = "word")
    @ExcludeForBDocs
    private Set<String> searchWords;

    public BriefDocument() {
        ;
    }

    public BriefDocument(DocumentEntity entity) {
        super();
        String ex = entity.getExtension().toLowerCase().trim();
        String nm = entity.getName().toLowerCase().trim();
        setExtName(ex);
        setName(nm);
        setUuid(entity.getId());
        addSearchWord(ex);
        for (String word : nm.split(" ")) {
            addSearchWord(word);
        }
    }

    public BriefDocument(DocumentEntity entity, Long perfId) {
        this(entity);
        setPerformerId(perfId);
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    public Long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(Long performerId) {
        this.performerId = performerId;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getExtName() {
        return this.extName;
    }

    public void setExtName(final String extName) {
        this.extName = extName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Set<String> getSearchWords() {
        return searchWords;
    }

    public void setSearchWords(Set<String> searchWords) {
        this.searchWords = searchWords;
    }

    public void addSearchWord(String word) {
        if (searchWords == null) {
            searchWords = new HashSet<>();
        }
        searchWords.add(word);
    }
}
