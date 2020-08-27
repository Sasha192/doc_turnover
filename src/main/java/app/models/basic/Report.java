package app.models.basic;

import app.models.abstr.IdentityBaseEntity;
import app.models.mysqlviews.BriefJsonDocument;
import app.models.serialization.ExcludeForJsonReport;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "reports")
public class Report extends IdentityBaseEntity {

    @Column(name = "date")
    private Date date;

    @Column(name = "time")
    private Time time;

    @OneToMany(cascade = {CascadeType.REFRESH})
    @JoinTable(name = "reports_docs",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "doc_id")
    )
    @ExcludeForJsonReport
    private List<BriefDocument> documents;

    @OneToMany(cascade = {CascadeType.REFRESH})
    @JoinTable(name = "reports_docs",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "doc_id")
    )
    @Column(insertable = false, updatable = false)
    private List<BriefJsonDocument> docList;

    @OneToMany(cascade = {CascadeType.REFRESH})
    @JoinTable(name = "comment_post",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private List<ReportComment> comments;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH},
            mappedBy = "report")
    @ExcludeForJsonReport
    private Task task;

    public Report() {
        date = Date.valueOf(LocalDate.now());
        time = Time.valueOf(LocalTime.now());
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public List<BriefDocument> getDocuments() {
        return documents;
    }

    @Deprecated
    /**
     * @see Report#addDocument(List)
     * @see Report#addDocument(BriefDocument)
     */
    public void setDocuments(List<BriefDocument> documents) {
        this.documents = documents;
    }

    public void addDocument(List<BriefDocument> docs) {
        if (documents == null) {
            documents = new LinkedList<>();
        }
        for (BriefDocument doc : docs) {
            documents.add(doc);
        }
    }

    public void addDocument(BriefDocument doc) {
        if (documents == null) {
            documents = new LinkedList<>();
        }
        documents.add(doc);
    }

    public List<BriefJsonDocument> getDocList() {
        return docList;
    }

    public void setDocList(List<BriefJsonDocument> docList) {
        this.docList = docList;
    }

    public List<ReportComment> getComments() {
        return comments;
    }

    public void setComments(List<ReportComment> comments) {
        this.comments = comments;
    }

    public void addComment(ReportComment comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Report)) {
            return false;
        }
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getDate())
                .append(getTime())
                .toHashCode();
    }
}
