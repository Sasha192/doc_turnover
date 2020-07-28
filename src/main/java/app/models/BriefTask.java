package app.models;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "brief_task_json_view")
public class BriefTask
        implements Serializable {

    @Id
    @Access(AccessType.PROPERTY)
    private Long id;

    @Column(name = "task")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "modification_date")
    private Date modificationDate;

    @Column(name = "is_deadline")
    private boolean deadline;

    @Column(name = "status_name")
    private String status;

    @Column(name = "deadline")
    private Date deadlineDate;

    @Column(name = "control_date")
    private Date controlDate;

    @Column(name = "priority")
    private String priority;

    @Column(name = "perf_name")
    private String performerName;

    @Column(name = "perf_department")
    private String performerDepartment;

    @Column(name = "perf_id")
    private Long performerId;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "owner_department")
    private String ownerDepartment;

    @Column(name = "owner_id")
    private Long ownerId;

    private BriefTask() {
        ;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
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

    public boolean isDeadline() {
        return this.deadline;
    }

    public void setDeadline(final boolean deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public Date getDeadlineDate() {
        return this.deadlineDate;
    }

    public void setDeadlineDate(final Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public Date getControlDate() {
        return this.controlDate;
    }

    public void setControlDate(final Date controlDate) {
        this.controlDate = controlDate;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setPriority(final String priority) {
        this.priority = priority;
    }

    public String getPerformerName() {
        return this.performerName;
    }

    public void setPerformerName(final String performerName) {
        this.performerName = performerName;
    }

    public String getPerformerDepartment() {
        return this.performerDepartment;
    }

    public void setPerformerDepartment(final String performerDepartment) {
        this.performerDepartment = performerDepartment;
    }

    public Long getPerformerId() {
        return this.performerId;
    }

    public void setPerformerId(final Long performerId) {
        this.performerId = performerId;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerDepartment() {
        return this.ownerDepartment;
    }

    public void setOwnerDepartment(final String ownerDepartment) {
        this.ownerDepartment = ownerDepartment;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(final Long ownerId) {
        this.ownerId = ownerId;
    }
}
