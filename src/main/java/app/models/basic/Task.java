package app.models.basic;

import app.models.abstr.IdentityBaseEntity;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "tasks")
public class Task
        extends IdentityBaseEntity
        implements Serializable {

    @Column(name = "task")
    private String toDo;

    @Column(name = "description")
    private String description;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(name = "tasks_performers",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "performer_id"))
    private Set<Performer> performers;

    @ElementCollection
    @CollectionTable(name = "tasks_performers",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @Column(name = "performer_id")
    private Set<Long> performerIds;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(
            name = "tasks_documents",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "doc_id"))
    private Set<BriefDocument> document;

    @ElementCollection
    @CollectionTable(name = "tasks_documents",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @Column(name = "doc_id")
    private Set<Long> documentsIds;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "is_deadline")
    private Boolean deadline;

    @Column(name = "priority")
    private String priority;

    @Column(name = "deadline")
    private Date deadlineDate;

    @Column(name = "control_date")
    private Date controlDate;

    @ElementCollection
    @CollectionTable(name = "tasks_keys",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @Column(name = "key")
    private Set<String> keys;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private TaskStatus status;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "task_owner_id",
            insertable = false,
            updatable = false)
    private Performer taskOwner;

    @Column(name = "task_owner_id")
    private Long taskOwnerId;

    @Column(name = "modification_date")
    private Date modificationDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "report_id", referencedColumnName = "id")
    private Report report;

    public Task() {
        Date now = Date.valueOf(LocalDate.now());
        setCreationDate(now);
        setDeadlineDate(now);
        setControlDate(now);
        setModificationDate(now);
    }

    public Long getTaskOwnerId() {
        return taskOwnerId;
    }

    public void setTaskOwnerId(Long taskOwnerId) {
        this.taskOwnerId = taskOwnerId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Set<Performer> getPerformers() {
        return performers;
    }

    public void setPerformers(Set<Performer> performers) {
        this.performers = performers;
    }

    public Set<BriefDocument> getDocument() {
        return this.document;
    }

    public void setDocument(final Set<BriefDocument> document) {
        this.document = document;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setCreationDate(final long creationDate) {
        this.creationDate = new Date(creationDate);
    }

    public Boolean getDeadline() {
        return this.deadline;
    }

    public void setDeadline(final Boolean deadline) {
        this.deadline = deadline;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setPriority(final String priority) {
        this.priority = priority;
    }

    public String getToDo() {
        return this.toDo;
    }

    public void setToDo(final String toDo) {
        this.toDo = toDo;
    }

    public Date getDeadlineDate() {
        return this.deadlineDate;
    }

    public void setDeadlineDate(final Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public void setDeadlineDate(final long deadlineDate) {
        this.deadlineDate = new Date(deadlineDate);
    }

    public Date getControlDate() {
        return this.controlDate;
    }

    public void setControlDate(final Date controlDate) {
        this.controlDate = controlDate;
    }

    public void setControlDate(final long controlDate) {
        this.controlDate = new Date(controlDate);
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public void setStatus(final TaskStatus status) {
        this.status = status;
    }

    public String getStatusString() {
        return getStatus().getName();
    }

    public Set<String> getKeys() {
        return this.keys;
    }

    public void setKeys(final Set<String> keys) {
        this.keys = keys;
    }

    public Performer getTaskOwner() {
        return this.taskOwner;
    }

    public void setTaskOwner(final Performer taskOwner) {
        this.taskOwner = taskOwner;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Date getModificationDate() {
        return this.modificationDate;
    }

    public void setModificationDate(final Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Set<Long> getPerformerIds() {
        return performerIds;
    }

    public void setPerformerIds(Set<Long> performerIds) {
        this.performerIds = performerIds;
    }

    public Set<Long> getDocumentsIds() {
        return documentsIds;
    }

    public void setDocumentsIds(Set<Long> documentsIds) {
        this.documentsIds = documentsIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Task)) {
            return false;
        }

        Task task = (Task) o;

        return new EqualsBuilder()
                .append(getToDo(), task.getToDo())
                .append(getDescription(), task.getDescription())
                .append(getCreationDate(), task.getCreationDate())
                .append(getDeadline(), task.getDeadline())
                .append(getPriority(), task.getPriority())
                .append(getDeadlineDate(), task.getDeadlineDate())
                .append(getControlDate(), task.getControlDate())
                .append(getKeys(), task.getKeys())
                .append(getStatus(), task.getStatus())
                .append(getTaskOwner(), task.getTaskOwner())
                .append(getModificationDate(), task.getModificationDate())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getToDo())
                .append(getDescription())
                .append(getCreationDate())
                .append(getDeadline())
                .append(getPriority())
                .append(getDeadlineDate())
                .append(getControlDate())
                .append(getKeys())
                .append(getStatus())
                .append(getTaskOwner())
                .append(getModificationDate())
                .toHashCode();
    }
}
