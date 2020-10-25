package app.customtenant.models.basic.taskmodels;

import app.configuration.spring.appfactory.ApplicationFactory;
import app.controllers.dto.TaskDto;
import app.customtenant.eventdriven.publishers.TaskEventPublisher;
import app.customtenant.models.abstr.IdentityBaseEntity;
import app.customtenant.models.basic.BriefDocument;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.basic.Report;
import app.customtenant.models.basic.TaskStatus;
import app.customtenant.models.serialization.ExcludeForJsonBriefTask;
import app.tenantconfiguration.TenantContext;
import app.utils.CustomAppDateTimeUtil;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tasks")
public class Task
        extends IdentityBaseEntity
        implements Serializable {

    @Transient
    private TaskEventPublisher publisher;

    @Column(name = "creation_time")
    private long creationTime;

    @Column(name = "is_deadline")
    private boolean deadline = false;

    @Column(name = "task")
    private String name;

    @Column(name = "deadline")
    private Date deadlineDate;

    @Column(name = "control_date")
    private Date controlDate;

    @Column(name = "description")
    private String description;

    @Column(name = "status_id")
    @Enumerated(EnumType.ORDINAL)
    private TaskStatus status;

    @Column(name = "task_owner_id")
    private long taskOwnerId;

    @ManyToOne(cascade = {CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinColumn(name = "task_owner_id",
            insertable = false,
            updatable = false)
    private Performer taskOwner;

    @ElementCollection
    @CollectionTable(name = "tasks_performers",
            joinColumns = @JoinColumn(name = "task_id",
                    referencedColumnName = "id")
    )
    @Column(name = "performer_id")
    private Set<Long> performerIds;

    @ElementCollection
    @CollectionTable(name = "tasks_documents",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @Column(name = "doc_id")
    private Set<Long> documentsIds;

    @Column(name = "report_id")
    @ExcludeForJsonBriefTask
    private Long reportId;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "report_id",
            referencedColumnName = "id",
            updatable = false, insertable = false)
    @ExcludeForJsonBriefTask
    private Report report;

    @ElementCollection
    @CollectionTable(name = "tasks_keys",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @Column(name = "key")
    @ExcludeForJsonBriefTask
    private Set<String> keys;

    @JoinTable(name = "tasks_performers",
            joinColumns = @JoinColumn(name = "task_id",
                    referencedColumnName = "id",
                    insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "performer_id"))
    @OneToMany(cascade = CascadeType.DETACH)
    @ExcludeForJsonBriefTask
    private Set<Performer> performers;

    @JoinTable(name = "tasks_documents",
            joinColumns = @JoinColumn(name = "task_id",
                    referencedColumnName = "id",
                    insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "doc_id"))
    @OneToMany(cascade = CascadeType.DETACH)
    @ExcludeForJsonBriefTask
    private Set<BriefDocument> documents;

    public Task() {
        this.creationTime = System.currentTimeMillis();
        this.deadlineDate = CustomAppDateTimeUtil.now();
        this.controlDate = deadlineDate;
        this.status = TaskStatus.NEW;
    }

    public Task(TaskDto dto, long taskOwnerIdd)
            throws ParseException {
        this();
        this.taskOwnerId = taskOwnerIdd;
        this.status = TaskStatus.getByName(dto.getStatus());
        this.controlDate = CustomAppDateTimeUtil.parse(dto.getDateControl());
        this.deadlineDate = CustomAppDateTimeUtil.parse(dto.getDeadline());
        this.description = dto.getDescription();
        this.name = dto.getName();
        this.documentsIds = new HashSet<>();
        this.performerIds = new HashSet<>();
        this.keys = new HashSet<>();
        Collections.addAll(keys, dto.getKeyWords());
        Collections.addAll(keys, name.trim().toLowerCase().split(" "));
        Collections.addAll(keys, description.trim().toLowerCase().split(" "));
        Collections.addAll(documentsIds, dto.getDocList());
        Collections.addAll(performerIds, dto.getPerformerList());
    }

    public TaskEventPublisher getPublisher() {
        return publisher;
    }

    public Long getTaskOwnerId() {
        return taskOwnerId;
    }

    public void setTaskOwnerId(Long taskOwnerId) {
        this.taskOwnerId = taskOwnerId;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Boolean getDeadline() {
        return this.deadline;
    }

    public String getToDo() {
        return this.name;
    }

    public void setToDo(final String toDo) {
        this.name = toDo;
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

    public Set<Performer> getPerformers() {
        return performers;
    }

    public void setPerformers(Set<Performer> performers) {
        this.performers = performers;
    }

    public Set<BriefDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<BriefDocument> documents) {
        this.documents = documents;
    }

    public void setStatus(final TaskStatus newState) {
        if (null != newState) {
            TaskStatus oldState = getStatus();
            if (oldState != null) {
                boolean check = TaskStatusValidator
                        .check(oldState, newState);
                if (check && null != getId()
                        && checkPublisher()) {
                    publisher.onStatusUpdate(this, oldState, newState, TenantContext.getTenant());
                }
            }
            this.status = newState;
        }
    }

    public void setDeadline(final Boolean deadline) {
        this.deadline = deadline;
        if (null != getId() && deadline
                && checkPublisher()) {
            publisher.onDeadlineSet(this, TenantContext.getTenant());
        }
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

    public void addDocumentId(Long docId) {
        if (documentsIds == null) {
            documentsIds = new HashSet<>();
        }
        documentsIds.add(docId);
    }

    public void setDocumentsIds(Set<Long> documentsIds) {
        this.documentsIds = documentsIds;
    }

    private boolean checkPublisher() {
        if (publisher == null) {
            publisher = (TaskEventPublisher)
                    ApplicationFactory
                            .getBean(TaskEventPublisher.class);
        }
        return publisher != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }

        if (!super.equals(o)) {
            return false;
        }

        Task task = (Task) o;

        if (creationTime != task.creationTime) {
            return false;
        }
        if (!name.equals(task.name)) {
            return false;
        }

        if (getStatus() != task.getStatus()) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (int) (creationTime ^ (creationTime >>> 32));
        result = 31 * result + getStatus().hashCode();
        return result;
    }
}
