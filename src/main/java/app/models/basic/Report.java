package app.models.basic;

import app.models.abstr.IdentityBaseEntity;
import app.models.mysqlviews.BriefJsonDocument;
import app.models.mysqlviews.BriefTask;
import app.models.serialization.ExcludeForJsonReport;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Date;
import java.sql.Time;
import java.util.Set;

@Entity
@Table(name = "reports")
public class Report extends IdentityBaseEntity {

    @Column(name = "date")
    private Date date;

    @Column(name = "time")
    private Time time;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "task_id")
    @ExcludeForJsonReport
    private Task task;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "reports_docs",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "doc_id")
    )
    private Set<BriefDocument> documents;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "reports_docs",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "doc_id")
    )
    private Set<BriefJsonDocument> docList;


    private Set<ReportComment> comments;

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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Set<BriefDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<BriefDocument> documents) {
        this.documents = documents;
    }

    public Set<BriefJsonDocument> getDocList() {
        return docList;
    }

    public void setDocList(Set<BriefJsonDocument> docList) {
        this.docList = docList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Report)) return false;
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
                .append(getTask())
                .toHashCode();
    }
}
