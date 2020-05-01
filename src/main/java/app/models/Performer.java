package app.models;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "performers")
public class Performer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "performer")
    private List<BriefDocument> briefDocuments;

    @OneToMany(mappedBy = "performer")
    private List<Task> tasks;

    @OneToMany(mappedBy = "performer")
    private List<Status> status;

    public Performer() {
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public List<BriefDocument> getBriefDocuments() {
        return this.briefDocuments;
    }

    public void setBriefDocuments(final List<BriefDocument> briefDocuments) {
        this.briefDocuments = briefDocuments;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(final List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Status> getStatus() {
        return this.status;
    }

    public void setStatus(final List<Status> status) {
        this.status = status;
    }
}
