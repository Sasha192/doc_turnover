package app.models;

import app.controllers.dto.PerformerDto;
import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "performers")
public class Performer
        extends IdentityEntity
        implements Serializable {

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tasks_performers",
            joinColumns = @JoinColumn(name = "performer_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    private List<Task> tasks;

    @OneToMany(mappedBy = "performer")
    private List<TaskStatus> status;

    @OneToOne(mappedBy = "performer", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
    private CustomUser user;

    public Performer() {
    }

    public Performer(PerformerDto dto) {

    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(final List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<TaskStatus> getStatus() {
        return this.status;
    }

    public void setStatus(final List<TaskStatus> status) {
        this.status = status;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Performer)) {
            return false;
        }
        final Performer performer = (Performer) o;
        return Objects.equal(this.getId(), performer.getId())
                && Objects.equal(this.getName(), performer.getName())
                && Objects.equal(this.getDepartment(), performer.getDepartment());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId(), this.getName(), this.getDepartment());
    }

    @Override
    public String toString() {
        return "Performer{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", department=" + department
                + '}';
    }

    public CustomUser getUser() {
        return this.user;
    }

    public void setUser(final CustomUser user) {
        this.user = user;
    }
}
