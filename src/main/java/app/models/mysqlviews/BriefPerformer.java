package app.models.mysqlviews;

import app.models.abstr.IdentityBaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
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

    //@Column(name = "email")
    @Transient
    private String email;

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
}