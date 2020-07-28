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
@Table(name = "json_view_brief_archive_doc")
public class BriefJsonDocument
        implements Serializable {

    @Id
    @Access(AccessType.PROPERTY)
    private Long id;

    @Column(name = "creation_date")
    private Date date;

    @Column(name = "file_name")
    private String name;

    @Column(name = "ext_name")
    private String extName;

    @Column(name = "task_count")
    private Long taskCount;

    @Column(name = "performer_id")
    private Long performerId;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "performer_name")
    private String performerName;

    @Column(name = "department_name")
    private String departmentName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public Long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Long taskCount) {
        this.taskCount = taskCount;
    }

    public Long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(Long performerId) {
        this.performerId = performerId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getPerformerName() {
        return this.performerName;
    }

    public void setPerformerName(final String performerName) {
        this.performerName = performerName;
    }

    public String getDepartmentName() {
        return this.departmentName;
    }

    public void setDepartmentName(final String departmentName) {
        this.departmentName = departmentName;
    }
}
