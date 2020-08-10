package app.models.basic;

import app.controllers.dto.PerformerDto;
import app.models.abstr.IdentityBaseEntity;
import app.models.serialization.ExcludeForJsonPerformer;
import app.security.models.SimpleRole;
import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.List;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "performers")
public class Performer
        extends IdentityBaseEntity
        implements Serializable {

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;

    @Column(name = "department_id")
    private Long departmentId;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(
            name = "tasks_performers",
            joinColumns = @JoinColumn(name = "performer_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    @ExcludeForJsonPerformer
    private List<Task> tasks;

    @OneToMany(mappedBy = "performer")
    @ExcludeForJsonPerformer
    private List<TaskStatus> status;

    @Column(name = "img_path")
    private String imgPath;

    @OneToOne(mappedBy = "performer")
    @ExcludeForJsonPerformer
    private CustomUser user;

    @ElementCollection
    @CollectionTable(name = "performer_user_roles",
            joinColumns = @JoinColumn(name = "performer_id")
    )
    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "role")
    private Set<SimpleRole> roles;

    public Performer() {
        imgPath = "/img/avatars/6.jpg";
    }

    public Performer(PerformerDto dto) {

        imgPath = "/img/avatars/6.jpg";
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

    /*public CustomUser getUser() {
        return this.user;
    }

    public void setUser(final CustomUser user) {
        this.user = user;
    }*/

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Set<SimpleRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SimpleRole> roles) {
        this.roles = roles;
    }
}
