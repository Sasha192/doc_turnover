package app.models;

import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "departments")
public class Department implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @Column(name = "department_name")
    private String name;

    @Column(name = "parent_department")
    private String parentDepartment;

    @OneToMany(mappedBy = "department")
    private Set<Performer> performers;

    public Department() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParentDepartment() {
        return this.parentDepartment;
    }

    public void setParentDepartment(final String parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public Set<Performer> getPerformers() {
        return this.performers;
    }

    public void setPerformers(final Set<Performer> performers) {
        this.performers = performers;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Department)) {
            return false;
        }
        final Department that = (Department) o;
        return Objects.equal(this.getId(), that.getId())
                && Objects.equal(this.getName(), that.getName())
                && Objects.equal(this.getParentDepartment(),
                that.getParentDepartment());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId(), this.getName(), this.getParentDepartment());
    }

    @Override
    public String toString() {
        return "Department{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", parentDepartment='" + parentDepartment + '\''
                + '}';
    }
}
