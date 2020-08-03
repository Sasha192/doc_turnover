package app.models.basic;

import app.models.abstr.IdentityBaseEntity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "custom_status")
public class TaskStatus
        extends IdentityBaseEntity
        implements Serializable {

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "performer_id")
    private Performer performer;

    @Column(name = "is_custom")
    private Boolean custom;

    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY,
    cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private List<Task> taskList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Performer getPerformer() {
        return this.performer;
    }

    public void setPerformer(final Performer performer) {
        this.performer = performer;
    }

    public Boolean getCustom() {
        return this.custom;
    }

    public void setCustom(final Boolean custom) {
        this.custom = custom;
    }

    public List<Task> getTaskList() {
        return this.taskList;
    }

    public void setTaskList(final List<Task> taskList) {
        this.taskList = taskList;
    }

    public void addTask(Task task) {
        if (taskList == null) {
            taskList = new LinkedList<>();
        }
        taskList.add(task);
    }
}
