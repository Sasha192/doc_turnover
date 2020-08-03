package app.models.basic;

import app.models.abstr.IdentityBaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "task_id")
    private Task task;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "reports_docs",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "doc_id")
    )
    private Set<BriefDocument> documents;

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
}
