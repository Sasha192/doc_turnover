package app.models;

import com.google.gson.annotations.Expose;
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
public class Department implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Integer id;

    @Column(name = "department_name")
    @Expose
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
