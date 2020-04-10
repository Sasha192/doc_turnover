package app.models;

import javax.persistence.Column;
import java.io.Serializable;

public class Department extends DomainObject implements Serializable {

    @Column(name = "department_name")
    private String departmentName;

    public Department() {
    }

    public String getDepartmentName() {
        return this.departmentName;
    }

    public void setDepartmentName(final String departmentName) {
        this.departmentName = departmentName;
    }
}
