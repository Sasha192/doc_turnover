package app.security.models;

import app.models.basic.CustomUser;
import app.models.basic.Department;
import app.models.basic.Performer;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class DefaultUserDetails implements UserDetails {

    private String email;

    private String password;

    private Set<GrantedAuthority> authorities = new HashSet<>();

    private boolean enabled;

    private Department department;

    public DefaultUserDetails(final CustomUser customUser) {
        Preconditions.checkNotNull(customUser);
        this.setPassword(customUser.getPassword());
        this.setUserName(customUser.getEmail());
        this.setEnabled(customUser.isEnabled());
        Performer performer = customUser.getPerformer();
        if (performer != null) {
            Department department = performer.getDepartment();
            if (department != null) {
                this.setDepartment(department);
            }
        }
        setAuthorities(mapAuthorities(customUser.getRoles()));
    }

    public static Set<GrantedAuthority> mapAuthorities(
            Set<SimpleRole> roles
    ) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (final SimpleRole role : roles) {
            switch (role) {
                case PERFORMER: {
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                    break;
                }
                case ADMIN: {
                    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    break;
                }
                case MANAGER: {
                    authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
                    break;
                }
                case G_MANAGER: {
                    authorities.add(new SimpleGrantedAuthority("ROLE_G_MANAGER"));
                    break;
                }
                case SECRETARY: {
                    authorities.add(new SimpleGrantedAuthority("SECRETARY"));
                    break;
                }
                default:
                    break;
            }
        }
        return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setUserName(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void addAuthority(GrantedAuthority authority) {
        this.authorities.add(authority);
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
