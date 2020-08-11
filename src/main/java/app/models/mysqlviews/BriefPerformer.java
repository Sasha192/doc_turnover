package app.models.mysqlviews;

import app.models.abstr.IdentityBaseEntity;

import javax.persistence.*;

import app.security.models.SimpleRole;
import org.hibernate.annotations.Immutable;

import java.util.Set;

@Entity
@Table(name = "brief_performer")
@Immutable
public class BriefPerformer
        extends IdentityBaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "department_name")
    private String department;

    @Column(name = "img_path")
    private String imgPath;

    @JoinTable(name = "users", joinColumns = {
            @JoinColumn(name = "performer_id", insertable = false, updatable = false)
    })
    @Column(name = "email")
    private String email;

    @ElementCollection
    @CollectionTable(name = "performer_user_roles",
            joinColumns = @JoinColumn(name = "performer_id")
    )
    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "role")
    private Set<SimpleRole> roles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
