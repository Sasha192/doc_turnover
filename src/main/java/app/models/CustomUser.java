package app.models;

import app.models.serialization.ExcludeForJsonPerformer;
import app.security.models.SimpleRole;
import app.security.models.UserDto;
import app.security.utils.DefaultPasswordEncoder;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
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
@Table(name = "users")
public class CustomUser
        extends IdentityBaseEntity
        implements Serializable {

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "enable")
    private boolean enabled;

    @ElementCollection
    @CollectionTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "role")
    private Set<SimpleRole> roles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "performer_id", referencedColumnName = "id")
    private Performer performer;

    public CustomUser() {
        ;
    }

    public CustomUser(UserDto dto, DefaultPasswordEncoder encoder) {
        Preconditions.checkNotNull(dto);
        setEmail(dto.getEmail());
        setEnabled(true);
        String encodedPassword = encoder == null
                ? dto.getPassword()
                : encoder.encode(dto.getPassword());
        setPassword(encodedPassword);
        addRole(SimpleRole.USER);
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

    public Set<SimpleRole> getRoles() {
        return this.roles;
    }

    public void setRoles(final Set<SimpleRole> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public Performer getPerformer() {
        return this.performer;
    }

    public void setPerformer(final Performer performer) {
        this.performer = performer;
    }

    public void addRole(final SimpleRole role) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(role);
    }
}
