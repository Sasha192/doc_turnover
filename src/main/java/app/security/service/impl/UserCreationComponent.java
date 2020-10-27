package app.security.service.impl;

import app.security.models.UserDto;
import app.security.models.auth.CustomUser;
import app.security.service.IUserCreation;
import app.security.service.IUserService;
import app.security.utils.DefaultPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCreationComponent implements IUserCreation {

    private final IUserService userService;

    private final DefaultPasswordEncoder encoder;

    @Autowired
    public UserCreationComponent(IUserService userService,
                                 DefaultPasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public CustomUser create(UserDto dto) {
        CustomUser customUser = new CustomUser(dto, encoder);
        userService.create(customUser);
        return customUser;
    }

    @Override
    public CustomUser create(CustomUser user) {
        userService.create(user);
        return user;
    }
}
