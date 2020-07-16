package app.models;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.*;

@Entity
@Table(name = "json_view_brief_archive_doc")
public class BriefJsonDocument implements Serializable {

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

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
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

    public Long getTaskCount() {
        return this.taskCount;
    }

    public void setTaskCount(final Long taskCount) {
        this.taskCount = taskCount;
    }

    public Long getPerformerId() {
        return this.performerId;
    }

    public void setPerformerId(final Long performerId) {
        this.performerId = performerId;
    }

    public Long getDepartmentId() {
        return this.departmentId;
    }

    public void setDepartmentId(final Long departmentId) {
        this.departmentId = departmentId;
    }
}
