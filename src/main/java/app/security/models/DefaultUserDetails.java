package app.security.models;

import app.models.CustomUser;
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

    public DefaultUserDetails(CustomUser customUser) {
        Preconditions.checkNotNull(customUser);
        setPassword(customUser.getPassword());
        setUserName(customUser.getEmail());
        setEnabled(customUser.isEnabled());
        for (SimpleRole role : customUser.getRoles()) {
            switch (role) {
                case USER: {
                    addAuthority(new SimpleGrantedAuthority("ROLE_USER"));
                    break;
                }
                case ADMIN: {
                    addAuthority(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    break;
                }
                default:
                    break;
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return enabled;
    }

    public void setUserName(final String email) {
        this.email = email;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setAuthorities(final Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public void addAuthority(final GrantedAuthority authority) {
        authorities.add(authority);
    }
}
