package app.models;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.sql.Date;
import javax.persistence.*;

@Entity
@Table(name = "brief_documents")
public class BriefDocument implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;

    @Column(name = "creation_date")
    @Expose
    private Date date;

    @Column(name = "file_name")
    @Expose
    private String name;

    @Column(name = "ext_name")
    @Expose
    private String extName;

    @Column(name = "full_path")
    private String path;

    @OneToOne(mappedBy = "document", cascade = CascadeType.ALL)
    @Expose
    private Task task;

    @ManyToOne
    @JoinColumn(name = "performer_id")
    @Expose
    private Performer performer;

    public BriefDocument() {
        ;
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(final Task task) {
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
}
