package app.models.basic;

import app.models.abstr.IdentityBaseEntity;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "brief_documents")
public class BriefDocument
        extends IdentityBaseEntity
        implements Serializable {

    @Column(name = "creation_date")
    private Date date;

    @Column(name = "file_name")
    private String name;

    @Column(name = "ext_name")
    private String extName;

    @Column(name = "full_path")
    private String path;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(
            name = "tasks_documents",
            joinColumns = @JoinColumn(name = "doc_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    private List<Task> task;

    @ManyToOne
    @JoinColumn(name = "performer_id")
    private Performer performer;

    @Transient
    private Set<Long> taskIds;

    public BriefDocument() {
        ;
    }

    public List<Task> getTask() {
        return this.task;
    }

    public void setTask(final List<Task> task) {
        this.task = task;
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

    public String getPath() {
        return this.path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Performer getPerformer() {
        return performer;
    }

    public void setPerformer(Performer performer) {
        this.performer = performer;
    }

    public Set<Long> getTaskIds() {
        if (taskIds == null || taskIds.isEmpty()) {
            taskIds = new HashSet<>();
            getTask().stream().forEach(task -> taskIds.add(task.getId()));
        }
        return this.taskIds;
    }

    public void setTaskIds(final Set<Long> taskIds) {
        this.taskIds = taskIds;
    }
}
