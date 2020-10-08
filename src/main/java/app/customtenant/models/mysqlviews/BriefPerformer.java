package app.customtenant.models.mysqlviews;

import app.customtenant.models.abstr.IdentityBaseEntity;
import app.security.models.SimpleRole;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;

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
