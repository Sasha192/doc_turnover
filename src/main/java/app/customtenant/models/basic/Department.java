package app.customtenant.models.basic;

import app.customtenant.models.abstr.IdentityBaseEntity;
import com.google.common.base.Objects;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "departments")
public class Department
        extends IdentityBaseEntity
        implements Serializable {

    @Column(name = "department_name")
    private String name;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "parent_department_id",
            insertable = false,
            updatable = false)
    private Department parentDepartment;

    @Column(name = "parent_department_id")
    private Long parentDepartmentId;

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

    public Department getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(Department parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public Long getParentDepartmentId() {
        return parentDepartmentId;
    }

    public void setParentDepartmentId(Long parentDepartmentId) {
        this.parentDepartmentId = parentDepartmentId;
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
