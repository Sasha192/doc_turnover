package app.security.models.auth;

import app.customtenant.models.abstr.IdentityBaseEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_info", schema = "bcrew_default")
public class UserInfo
        extends IdentityBaseEntity
        implements Serializable {

    private static final String IMG_DEFAULT = "/img/default.jpg";

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "img_path")
    private String imgPath = IMG_DEFAULT;

    @Column(name = "img_id_token")
    private String imgIdToken;

    public UserInfo() {
        ;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgIdToken() {
        return imgIdToken;
    }

    public void setImgIdToken(String imgIdToken) {
        this.imgIdToken = imgIdToken;
    }
}
