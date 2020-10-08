package app.security.service.impl;

import app.security.models.DefaultUserDetails;
import app.security.models.auth.CustomUser;
import app.security.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName)
            throws UsernameNotFoundException {
        CustomUser customUser = userService.retrieveByName(userName);
        return new DefaultUserDetails(customUser);
    }
}
