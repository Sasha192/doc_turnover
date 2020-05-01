package app.models;

import java.io.Serializable;
import java.sql.Date;
import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tasks")
public class Task implements Serializable {

    public static final String WARNING = "warning";

    public static final String PRIMARY = "primary";

    public static final String DANGEROUS = "dangerous";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task")
    private String toDo;

    @ManyToOne
    @JoinColumn(name = "performer_id")
    private Performer performer;

    @OneToOne
    @JoinColumn(name = "doc_id")
    private BriefDocument document;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "modification_date")
    private Date modificationDate;

    @Column(name = "is_deadline")
    private Boolean deadline;

    @Column(name = "priority")
    private String priority;

    @Column(name = "deadline")
    private Date deadlineDate;

    @Column(name = "control_date")
    private Date controlDate;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    private String statusString;

    public Task() {
        ;
    }

    @PostConstruct
    private void init() {
        statusString = getStatus().getName();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Performer getPerformer() {
        return this.performer;
    }

    public void setPerformer(final Performer performer) {
        this.performer = performer;
    }

    public BriefDocument getDocument() {
        return this.document;
    }

    public void setDocument(final BriefDocument document) {
        this.document = document;
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

    public Date getControlDate() {
        return this.controlDate;
    }

    public void setControlDate(final Date controlDate) {
        this.controlDate = controlDate;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public String getStatusString() {
        return this.statusString;
    }

    public void setStatusString(final String statusString) {
        this.statusString = statusString;
    }
}
