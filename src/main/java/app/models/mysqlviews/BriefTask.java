package app.models.mysqlviews;

import app.models.abstr.IdentityBaseEntity;
import app.models.basic.Performer;
import app.models.basic.Report;
import app.models.basic.TaskComment;
import app.models.serialization.ExcludeForJsonBriefTask;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "brief_task_json_view")
@Immutable
public class BriefTask
        extends IdentityBaseEntity {

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

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "owner_department")
    private String ownerDepartment;

    @Column(name = "owner_department_id")
    private String ownerDepartmentId;

    @Column(name = "owner_img_path")
    private String managerImgPath;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(
            name = "tasks_documents",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "doc_id")
    )
    @ExcludeForJsonBriefTask
    private List<BriefJsonDocument> docList;

    @ElementCollection
    @CollectionTable(
            name = "tasks_keys",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @Column(name = "key")
    private List<String> keys;

    @ManyToMany
    @JoinTable(name = "tasks_performers",
            joinColumns = @JoinColumn(name = "task_id",
                    insertable = false,
                    updatable = false),
            inverseJoinColumns = @JoinColumn(name = "performer_id"))
    private List<BriefPerformer> briefPerformers;

    @ManyToMany
    @JoinTable(name = "tasks_performers",
            joinColumns = @JoinColumn(name = "task_id",
                    insertable = false,
                    updatable = false),
            inverseJoinColumns = @JoinColumn(name = "performer_id"))
    @ExcludeForJsonBriefTask
    private List<Performer> performers;

    @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "report_id", referencedColumnName = "id")
    @ExcludeForJsonBriefTask
    private Report report;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "comment_post",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private List<TaskComment> comments;

    private BriefTask() {
        ;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
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

    public String getOwnerDepartmentId() {
        return ownerDepartmentId;
    }

    public void setOwnerDepartmentId(String ownerDepartmentId) {
        this.ownerDepartmentId = ownerDepartmentId;
    }

    public String getManagerImgPath() {
        return managerImgPath;
    }

    public void setManagerImgPath(String managerImgPath) {
        this.managerImgPath = managerImgPath;
    }

    public List<BriefJsonDocument> getDocList() {
        return docList;
    }

    public void setDocList(List<BriefJsonDocument> docList) {
        this.docList = docList;
    }

    public Report getReports() {
        return report;
    }

    public void setReports(Report reports) {
        this.report = reports;
    }

    public List<BriefPerformer> getBriefPerformers() {
        return briefPerformers;
    }

    public void setBriefPerformers(List<BriefPerformer> briefPerformers) {
        this.briefPerformers = briefPerformers;
    }

    public List<Performer> getPerformers() {
        return performers;
    }

    public void setPerformers(List<Performer> performers) {
        this.performers = performers;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public List<TaskComment> getComments() {
        return comments;
    }

    public void setComments(List<TaskComment> comments) {
        this.comments = comments;
    }
}
