package app.models.basic;

import app.models.abstr.IdentityBaseEntity;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
    private Set<Performer> performer;

    @Transient
    private Set<Long> performerIds;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(
            name = "tasks_documents",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "doc_id"))
    private Set<BriefDocument> document;

    @Transient
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

    @ManyToOne
    @JoinColumn(name = "status_id")
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "task_owner_id")
    private Performer taskOwner;

    @Column(name = "modification_date")
    private Date modificationDate;

    public Task() {
        Date now = Date.valueOf(LocalDate.now());
        setCreationDate(now);
        setDeadlineDate(now);
        setControlDate(now);
        setModificationDate(now);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Set<Performer> getPerformer() {
        return this.performer;
    }

    public void setPerformer(final Set<Performer> performer) {
        this.performer = performer;
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

    public Set<Long> getPerformerIds() {
        if (performerIds == null || performerIds.isEmpty()) {
            performerIds = new HashSet<>();
            getPerformer().stream().forEach(perf -> performerIds.add(perf.getId()));
        }
        return this.performerIds;
    }

    public void setPerformerIds(final Set<Long> performerIds) {
        this.performerIds = performerIds;
    }

    public Set<Long> getDocumentsIds() {
        if (documentsIds == null || documentsIds.isEmpty()) {
            documentsIds = new HashSet<>();
            getDocument().stream().forEach(doc -> performerIds.add(doc.getId()));
        }
        return this.documentsIds;
    }

    public void setDocumentsIds(final Set<Long> documentsIds) {
        this.documentsIds = documentsIds;
    }

    public Date getModificationDate() {
        return this.modificationDate;
    }

    public void setModificationDate(final Date modificationDate) {
        this.modificationDate = modificationDate;
    }
}
