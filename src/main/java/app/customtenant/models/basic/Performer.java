package app.customtenant.models.basic;

import app.customtenant.models.abstr.IdentityBaseEntity;
import app.customtenant.models.serialization.ExcludeForJsonPerformer;
import app.customtenant.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import app.security.models.SimpleRole;
import app.security.models.auth.CustomUser;
import app.security.models.auth.UserInfo;
import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "performers")
public class Performer
        extends IdentityBaseEntity
        implements Serializable {

    private static final String IMG_DEFAULT = "/img/default.jpg";

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "department_id")
    private long departmentId;

    @ManyToOne(cascade = {CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id",
            insertable = false, updatable = false)
    private Department department;

    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "role")
    private SimpleRole roles;

    @OneToMany(mappedBy = "performer", cascade = CascadeType.REFRESH)
    @ExcludeForJsonPerformer
    private Set<AbstractCalendarPerformerStatistic> statistics;

    public Performer(CustomUser user) {
        super();
        UserInfo info = user.getUserInfo();
        String name = info.getFirstName()
                + " "
                + info.getMiddleName()
                + " "
                + info.getLastName();
        setName(name);
        setRoles(SimpleRole.GUEST);
        setUserId(user.getId());
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<AbstractCalendarPerformerStatistic> getStatistics() {
        return statistics;
    }

    public void setStatistics(Set<AbstractCalendarPerformerStatistic> statistics) {
        this.statistics = statistics;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SimpleRole getRoles() {
        return roles;
    }

    public void setRoles(SimpleRole roles) {
        this.roles = roles;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void addRole(SimpleRole newRole) {
        this.roles = newRole;
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
}
