package app.models;

import app.models.serialization.NoOneToManySerialization;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "departments")
public class Department implements Serializable,
        NoOneToManySerialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "parent_department")
    private String parentDepartment;

    @OneToMany(mappedBy = "department")
    private Set<Performer> performers;

    public Department() {
    }

    public String getDepartmentName() {
        return this.departmentName;
    }

    public void setDepartmentName(final String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(final Integer id) {
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
}
