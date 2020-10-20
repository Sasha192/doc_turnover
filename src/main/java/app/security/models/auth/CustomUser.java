package app.security.models.auth;

import app.customtenant.models.abstr.IdentityBaseEntity;
import app.security.models.UserDto;
import app.security.utils.DefaultPasswordEncoder;
import app.utils.ImgToken;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users", schema = "bcrew_default")
public class CustomUser
        extends IdentityBaseEntity
        implements Serializable {

    private static final String IMG_DEFAULT = "/img/default.jpg";

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "enable")
    private boolean enabled;

    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "role")
    private ApplicationRoles role;

    @Column(name = "user_info_id")
    private Long userInfoId;

    @JoinColumn(name = "user_info_id",
            insertable = false,
            updatable = false)
    @OneToOne
    private UserInfo userInfo;

    @ElementCollection
    @CollectionTable(
            name = "users_tenants",
            schema = "bcrew_default",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "tenant_id")
    private Set<String> tenantsId;

    public CustomUser() {
        ;
    }

    public CustomUser(UserDto dto, DefaultPasswordEncoder encoder) {
        Preconditions.checkNotNull(dto);
        setLogin(dto.getEmail());
        setEnabled(true);
        String encodedPassword = encoder == null
                ? dto.getPassword()
                : encoder.encode(dto.getPassword());
        setPassword(encodedPassword);
        setRole(ApplicationRoles.ROLE_USER);
        UserInfo info = new UserInfo();
        info.setFirstName(dto.getFirstName());
        info.setLastName(dto.getLastName());
        info.setMiddleName(dto.getMiddleName());
        String imgToken = ImgToken.generate(info);
        info.setImgIdToken(imgToken);
        setUserInfo(info);
    }

    public Long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getEmail() {
        return login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public ApplicationRoles getRole() {
        return role;
    }

    public void setRole(ApplicationRoles role) {
        this.role = role;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }

    public Set<String> getTenants() {
        return tenantsId;
    }

    public void addTenant(String id) {
        if (tenantsId == null) {
            tenantsId = new HashSet<>();
        }
        tenantsId.add(id);
    }

    public void setTenantsId(Set<String> tenantsId) {
        this.tenantsId = tenantsId;
    }
}
