package app.models;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "brief_documents")
public class BriefDocument extends DomainObject implements Serializable {

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "modification_date")
    private Date modificationDate;

    @Column(name = "file_name")
    private String name;

    @Column(name = "full_path")
    private String path;

    @ManyToOne
    @JoinColumn(name = "performer_id")
    private Performer performer;

    @Column(name = "is_deadline")
    private Boolean deadline;

    @Column(name = "significance")
    private Integer significance;

    public BriefDocument() {
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return this.modificationDate;
    }

    public void setModificationDate(final Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Performer getPerformer() {
        return this.performer;
    }

    public void setPerformer(final Performer performer) {
        this.performer = performer;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(final String path) {
        this.path = path;
    }


    public Boolean getDeadline() {
        return this.deadline;
    }

    public void setDeadline(final Boolean deadline) {
        this.deadline = deadline;
    }

    public Integer getSignificance() {
        return this.significance;
    }

    public void setSignificance(final Integer significance) {
        this.significance = significance;
    }
}
