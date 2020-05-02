package app.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "brief_documents")
public class BriefDocument implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;

    @Column(name = "creation_date")
    @Expose
    private Date creationDate;

    @Column(name = "file_name")
    @Expose
    private String name;

    @Column(name = "full_path")
    private String path;

    @OneToOne(mappedBy = "document", cascade = CascadeType.ALL)
    @Expose
    private Task task;

    public BriefDocument() {
        ;
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(final Task task) {
        this.task = task;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
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
}
